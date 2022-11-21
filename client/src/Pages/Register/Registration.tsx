import React, {useEffect} from 'react';
import cn from "classnames";
import "./Registration.less"
import LoginForm from "./Forms/LoginForm/LoginForm";
import RegistrationForm from "./Forms/RegistrationForm/RegistrationForm";
import {useSearchParams} from "react-router-dom";

const Registration = () => {
    const [queryParams, setSearchParams] = useSearchParams();
    const [isRegistration, setIsRegistration] = React.useState(queryParams.get("type") !== "login");


    useEffect(() => {
        if (isRegistration) {
            setSearchParams({type: ""});
        } else {
            setSearchParams({type: "login"});
        }
    }, [isRegistration]);

    useEffect(() => {
        setIsRegistration(queryParams.get("type") !== "login");
    }, [queryParams]);

    return (
        <div className="register-form">
            <header className="register-types">
                <div onClick={() => setIsRegistration(true)}
                     className={cn("type", {"active-type": isRegistration})}>Регистрация
                </div>
                <div onClick={() => setIsRegistration(false)} className={cn("type", {"active-type": !isRegistration})}>Вход
                </div>
            </header>
            {isRegistration ? <RegistrationForm onRegistration={() => setIsRegistration(false)}/> : <LoginForm/>}
        </div>
    );
};

export default Registration;
