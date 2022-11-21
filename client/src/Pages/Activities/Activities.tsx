import React from 'react';
import cn from "classnames";
import "./Activities.less";
import CreateCard from "./Cards/CreateCard";

const Activities = () => {
    return (
        <div className={cn("activities")}>
            <CreateCard />
        </div>
    );
};

export default Activities;
