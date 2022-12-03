import React, {useEffect, useState} from 'react';
import cn from "classnames";
import "./Activities.less";
import CreateCard from "./Cards/CreateCard";
import {getAllActivities} from "../../http/activitiesApi";
import {ActivityFullInfo} from "../../models/ActivityFullInfo";
import ActivityCard from "./Cards/ActivityCard";
import {ColorRing} from 'react-loader-spinner'

const Activities = () => {
    const [activities, setActivities] = useState<ActivityFullInfo[]>([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadActivities();
    }, []);

    const onSave = async () => {
        await loadActivities();
    }

    return (
        <div className={cn("activities")}>
            {activities.map((info) => <ActivityCard key={info.id} info={info}/>)}
            <CreateCard onSave={onSave}/>
            <ColorRing
                visible={loading}
                height="80"
                width="80"
                ariaLabel="blocks-loading"
                wrapperStyle={{}}
                wrapperClass="blocks-wrapper"
                colors={['#e15b64', '#f47e60', '#f8b26a', '#abbd81', '#849b87']}
            />
        </div>
    );

    async function loadActivities() {
        setLoading(true);
        try {
            const activities = await getAllActivities() || [];
            setActivities(activities);
        } finally {
            setTimeout(() => {
                setLoading(false);
            }, 1000);
        }
    }
};

export default Activities;
