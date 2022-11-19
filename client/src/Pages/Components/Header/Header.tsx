import React from 'react';
import "./Header.less";
import cn from "classnames";
import {HiOutlineUserCircle} from 'react-icons/hi';
import {useLocation, useNavigate} from 'react-router-dom';
import {getAvatarPopMenuItems} from "./helpers";
import {MAIN_ROUTE, REGISTER_ROUTE} from "../../../http/routes";
import useUserContext from "../../../hooks/useUserContext";

const Header = () => {
    const [showPopupMenu, setShowPopupMenu] = React.useState(false);
    const user = useUserContext();
    const navigate = useNavigate();
    const location = useLocation();
    const popupItems = getAvatarPopMenuItems(location.pathname, user);

    const logout = () => {
        user?.setIsAuth(false);
        user?.saveUserInfo(null);
        setShowPopupMenu(false);
        navigate(REGISTER_ROUTE + "?type=login");
    }
    return (
        <header className={cn("header")}>
            <span className={cn("caption")} onClick={() => navigate(MAIN_ROUTE)}>Jikan</span>
            <div className={cn("avatar")} onMouseEnter={() => setShowPopupMenu(true)}
                 onMouseLeave={() => setShowPopupMenu(false)}>
                <HiOutlineUserCircle size={56}/>
                {showPopupMenu &&
                <div className={cn("popup-menu")}>
                    {popupItems?.map(({caption, url}) => {
                        if (caption === "Выйти") {
                            return <div key={caption}
                                        onClick={logout}
                                        className={cn("menu-item")}>{caption}</div>;
                        }
                        return <div key={caption}
                                    onClick={() => url && navigate(url)}
                                    className={cn("menu-item")}>{caption}</div>;
                    })}
                </div>
                }
            </div>
        </header>
    );
};

export default Header;
