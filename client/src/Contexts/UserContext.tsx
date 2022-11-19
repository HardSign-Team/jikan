import React, {createContext} from "react";
import {UserModel} from "../models/UserModel";

export interface UserInfoContext {
    isAuth: boolean;
    userInfo: Nullable<UserModel>;
    saveUserInfo: (user: UserModel | null) => void;
    setIsAuth: (isAuth: boolean) => void;
}

const UserContext = createContext<UserInfoContext | null>(null);

interface UserProviderProps {
    children: React.ReactNode;
    userInfo: {
        isAuth: boolean;
        userInfo: Nullable<UserModel>;
    };
}

export const UserProvider = ({children, userInfo}: UserProviderProps) => {
    const [currentUser, setCurrentUser] = React.useState<UserModel | null>(
        userInfo?.userInfo ?? null
    );

    const [isAuthUser, setIsAuthUser,] = React.useState(userInfo?.isAuth ?? false);

    const saveUserInfo = (user: UserModel | null) => {
        setCurrentUser(user);
    };

    const setIsAuth = (isAuth: boolean) => {
        console.log(isAuth);
        setIsAuthUser(isAuth);
    }

    return (
        <UserContext.Provider
            value={{isAuth: isAuthUser, userInfo: currentUser, saveUserInfo, setIsAuth}}
        >
            {children}
        </UserContext.Provider>
    );
};

export const UserConsumer = UserContext.Consumer;

export default UserContext;