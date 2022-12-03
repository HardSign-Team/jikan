import React, {useEffect, useState} from 'react';
import "./Cabinet.less";
import cn from "classnames";
import useUserContext from "../../hooks/useUserContext";
import {getUser} from "../../http/userApi";
import {ColorRing} from "react-loader-spinner";

const Cabinet = () => {
    const userInfo = useUserContext();
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchUser();
    }, []);

    console.log(userInfo);
    return (
        <div className={cn("cabinet")}>
            <ColorRing
                visible={loading}
                height="80"
                width="80"
                ariaLabel="blocks-loading"
                wrapperStyle={{}}
                wrapperClass="blocks-wrapper"
                colors={['#b8c480', '#B2A3B5', '#F4442E', '#51E5FF', '#429EA6']}
            />
            {!loading &&
            (<>
                <div>Имя: {userInfo?.userInfo?.name}</div>
                <div>Логин: {userInfo?.userInfo?.login}</div>
                <div>UserId: {userInfo?.userInfo?.id}</div>
            </>)
            }

        </div>
    );

    async function fetchUser() {
        if (!userInfo?.userInfo?.login) {
            return;
        }
        setLoading(true);
        try {
            const user = await getUser(userInfo?.userInfo?.login);
            userInfo?.saveUserInfo(user);
        } finally {
            setTimeout(() => {
                setLoading(false);
            }, 1000);
        }
    }
};

export default Cabinet;
