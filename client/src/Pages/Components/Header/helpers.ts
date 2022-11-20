import {UserInfoContext} from "../../../Contexts/UserContext";

const items = [
    {
        caption: "Войти",
        url: "/register?type=login"
    },
    {
        caption: "Помощь",
        url: "/help"
    },
    {
        caption: "О нас",
        url: "/about"
    },
    {
        caption: "Выйти",
    },
]

export const getAvatarPopMenuItems = (path: string, user: UserInfoContext | null) => {
    if (user?.isAuth) {
        return items.slice(1, 4);
    }
    return items.slice(0, 3);
}