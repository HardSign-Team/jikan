import React, {useEffect, useState} from 'react';
import "./ActivityCard.less";
import cn from "classnames";
import {ActivityFullInfo, TimestampModel} from "../../../models/ActivityFullInfo";
import {getAllTimestamps, getLastTimestampByActivityId, startActivity, stopActivity} from "../../../http/timestampApi";
import {ColorRing} from "react-loader-spinner";
import {useThrottle} from "../../../hooks/useDebounce";

interface ActivityCardProps {
    info: ActivityFullInfo;
}

const ActivityCard = ({info}: ActivityCardProps) => {
    const {id, name} = info;

    const [isStarted, setIsStarted] = useState(false);
    const [lastTimestamp, setLastTimestamp] = useState<Nullable<TimestampModel>>(null);
    const [overallActivity, setOverallActivity] = useState<Nullable<number>>(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadActivityInfo();
    }, []);

    useEffect(() => {
        const timer = setInterval(() => {
            setOverallActivity(prev => {
                if (isStarted) {
                    return (prev ?? 0) + 1000;
                }
                return prev;
            });
        }, 1000);
        return () => clearTimeout(timer);
    }, [isStarted]);


    const onClick = useThrottle(async () => {
        setLoading(true);
        try {
            if (isStarted) {
                const timestamp = await stopActivity(id);
                setLastTimestamp(timestamp);
                await loadActivityInfo();
            } else {
                await startActivity(id);
            }
        } finally {
            setTimeout(() => {
                setLoading(false);
                setIsStarted(!isStarted);
            }, 1000);
        }
    }, 1000);

    const getLastSeance = () => {
        if (lastTimestamp?.end) {
            return new Date(lastTimestamp?.end).toLocaleString();
        }
        if (lastTimestamp?.start) {
            return new Date(lastTimestamp?.start).toLocaleString();
        }
        return "Пусто";
    }


    const getDate = (overallActivity: number) => {
        let days = overallActivity / (24 * 60 * 60 * 1000);
        let hours = (days % 1) * 24;
        let minutes = (hours % 1) * 60;
        let secs = (minutes % 1) * 60;
        [days, hours, minutes, secs] = [Math.floor(days), Math.floor(hours), Math.floor(minutes), Math.floor(secs)]
        return (days !== 0 ? `${days}d ` : "")
            + (hours !== 0 ? `${hours}h ` : "")
            + (minutes !== 0 ? `${minutes}m ` : "")
            + (secs !== 0 ? `${secs}s` : "");
    }
    return (
        <div key={id} className={cn("activity-card")}>
            <div className={cn("activity-header")} title={name}>{name}</div>
            <div>Время активности</div>
            <div>{overallActivity ? getDate(overallActivity) : "0s"}</div>
            <div>Последний сеанс</div>
            <div>{getLastSeance()}</div>
            <button onClick={onClick}
                    className={cn("button", {stop: isStarted})}>
                {loading ?
                    <ColorRing
                        visible={loading}
                        height="20"
                        width="20"
                        ariaLabel="blocks-loading"
                        wrapperStyle={{}}
                        wrapperClass={cn("activity-card-loader")}
                        colors={['#e15b64', '#f47e60', '#f8b26a', '#abbd81', '#849b87']}
                    /> :
                    isStarted ? "Остановить" : "Начать"}
            </button>
        </div>
    );

    async function loadActivityInfo() {
        const [lastTimestamp, allTimestamps] = await Promise.all([getLastTimestampByActivityId(id), getAllTimestamps(id)]);
        setLastTimestamp(lastTimestamp);
        if (lastTimestamp?.start && !lastTimestamp?.end) {
            setIsStarted(true);
        }
        if (allTimestamps?.length) {
            setOverallActivity(calculateOverallActivity(allTimestamps));
        }
    }

    function calculateOverallActivity(allTimestamps: TimestampModel[]): number {
        let result = 0;
        allTimestamps.forEach(({start, end}) => {
            const endDate = new Date(end);
            const startDate = new Date(start);
            const d = endDate.getTime() - startDate.getTime();
            result += d < 0 ? 0 : d;
        })
        console.log(result);
        return Math.ceil(result);
    }
};

export default ActivityCard;
