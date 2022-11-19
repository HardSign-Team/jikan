import React, {useEffect} from 'react';
import cn from "classnames";
import "./Registration.less"
import LoginForm from "./Forms/LoginForm/LoginForm";
import RegistrationForm from "./Forms/RegistrationForm/RegistrationForm";
import {useSearchParams} from "react-router-dom";

const Registration = () => {
    const [queryParams, setSearchParams] = useSearchParams();
    const [isRegister, setIsRegister] = React.useState(queryParams.get("type") !== "login");


    useEffect(() => {
        if (isRegister) {
            setSearchParams({type: ""});
        } else {
            setSearchParams({type: "login"});
        }
    }, [isRegister]);

    useEffect(() => {
        setIsRegister(queryParams.get("type") !== "login");
    }, [queryParams]);

    return (
        <div className="register-form">
            <header className="register-types">
                <div onClick={() => setIsRegister(true)}
                     className={cn("type", {"active-type": isRegister})}>Регистрация
                </div>
                <div onClick={() => setIsRegister(false)} className={cn("type", {"active-type": !isRegister})}>Вход
                </div>
            </header>
            {isRegister ? <RegistrationForm onRegistration={() => setIsRegister(false)}/> : <LoginForm/>}
        </div>
    );
};

export default Registration;
