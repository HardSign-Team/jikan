import React from 'react';
import "./Cabinet.less";
import cn from "classnames";
import useUserContext from "../../hooks/useUserContext";

const Cabinet = () => {
    const userInfo = useUserContext();
    return (
        <div className={cn("cabinet")}>
            <div>Имя: {userInfo?.userInfo?.name}</div>
            <div>Логин: {userInfo?.userInfo?.login}</div>
            {userInfo?.userInfo?.userId && <div>UserID: {userInfo?.userInfo?.userId}</div>}
        </div>
    );
};

export default Cabinet;
