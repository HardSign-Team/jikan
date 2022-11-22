import React, {useEffect, useState} from 'react';
import cn from "classnames";
import "./Activities.less";
import CreateCard from "./Cards/CreateCard";
import {getAllActivities} from "../../http/activitiesApi";
import {ActivityFullInfo} from "../../models/ActivityFullInfo";
import ActivityCard from "./Cards/ActivityCard";

const Activities = () => {
    const [activities, setActivities] = useState<ActivityFullInfo[]>([]);
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
        </div>
    );

    async function loadActivities() {
        const activities = await getAllActivities() || [];
        setActivities(activities);
    }
};

export default Activities;
