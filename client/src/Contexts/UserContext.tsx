import React, {createContext} from "react";
import {UserModel} from "../models/UserModel";
import jwt_decode from "jwt-decode";
import {getNewAccessToken} from "../http/userApi";

export interface UserInfoContext {
    isAuth: boolean;
    userInfo: Nullable<UserModel>;
    saveUserInfo: (user: UserModel | null) => void;
    setIsAuth: (isAuth: boolean) => void;
}

const UserContext = createContext<UserInfoContext | null>(null);

interface UserProviderProps {
    children: React.ReactNode;
}

export const UserProvider = ({children}: UserProviderProps) => {
    const [currentUser, setCurrentUser] = React.useState<UserModel | null>(null);

    const [isAuthUser, setIsAuthUser] = React.useState(false);


    React.useEffect(() => {
        getUserInfo();
    }, []);

    const getUserInfo = async () => {
        const accessToken = localStorage.getItem('accessToken');
        let login = "", username = "";
        if (accessToken) {
            const {sub, name}: { sub: string, name: string } = jwt_decode(accessToken ?? "");
            login = sub;
            username = name;
        } else {
            try {
                const {sub, name} = await getNewAccessToken();
                login = sub;
                username = name;

            } catch {
                setIsAuth(false);
                setCurrentUser(null);
            }
        }
        setIsAuth(true);
        setCurrentUser({
            userId: null,
            name: username,
            login,
        });
    }

    const saveUserInfo = (user: UserModel | null) => {
        setCurrentUser(user);
    };

    const setIsAuth = (isAuth: boolean) => {
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