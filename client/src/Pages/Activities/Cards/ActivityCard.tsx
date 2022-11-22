import React, {useEffect, useState} from 'react';
import "./ActivityCard.less";
import cn from "classnames";
import {ActivityFullInfo, TimestampModel} from "../../../models/ActivityFullInfo";
import {getAllTimestamps, getLastTimestampByActivityId, startActivity, stopActivity} from "../../../http/timestampApi";

interface ActivityCardProps {
    info: ActivityFullInfo;
}

const ActivityCard = ({info}: ActivityCardProps) => {
    const {id, name} = info;

    const [isStarted, setIsStarted] = useState(false);
    const [lastTimestamp, setLastTimestamp] = useState<Nullable<TimestampModel>>(null);
    const [overallActivity, setOverallActivity] = useState<Nullable<number>>(null);

    useEffect(() => {
        loadActivityInfo();
    }, []);


    const onClick = async () => {
        if (isStarted) {
            const [timestamp,] = await Promise.all([stopActivity(id), loadActivityInfo()]);
            setLastTimestamp(timestamp);
        } else {
            await startActivity(id);
        }
        setIsStarted(!isStarted);
    }
    return (
        <div key={id} className={cn("activity-card")}>
            <div className={cn("activity-header")}>{name}</div>
            <div>Время активности</div>
            <div>{(overallActivity && (Math.ceil(overallActivity) === 0 ? `${overallActivity * 24} минут` : `${Math.ceil(overallActivity)} минут`)) ?? "Пусто"}</div>
            <div>Последний сеанс</div>
            <div>{lastTimestamp?.end ? new Date(lastTimestamp?.end).toLocaleDateString() : "Пусто"}</div>
            <button onClick={onClick}
                    className={cn("button", {stop: isStarted})}>{isStarted ? "Остановить" : "Начать"}</button>
        </div>
    );

    async function loadActivityInfo() {
        const [lastTimestamp, allTimestamps] = await Promise.all([getLastTimestampByActivityId(id), getAllTimestamps(id)]);
        setLastTimestamp(lastTimestamp);
        setOverallActivity(calculateOverallActivity(allTimestamps || []));
    }

    function calculateOverallActivity(allTimestamps: TimestampModel[]): number {
        let resultHours = 0;
        allTimestamps.forEach(({start, end}) => {
            const endDate = new Date(end);
            const startDate = new Date(start);
            const _MS_PER_DAY = 1000 * 60 * 60 * 24;
            resultHours += (toUtc(endDate) - toUtc(startDate)) / _MS_PER_DAY;
        })
        return Math.ceil(resultHours);
    }

    function toUtc({getHours, getDate, getMonth, getFullYear, getSeconds}: Date) {
        return Date.UTC(getFullYear(), getMonth(), getDate(), getHours(), getSeconds());
    }

};

export default ActivityCard;
