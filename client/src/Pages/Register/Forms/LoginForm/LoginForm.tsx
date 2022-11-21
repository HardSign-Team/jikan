import React, {useState} from 'react';
import cn from "classnames";
import "../RegistrationForm/RegistrationForm.less"
import {useForm} from "react-hook-form";
import {login} from "../../../../http/userApi";
import {useNavigate} from "react-router-dom";
import {MAIN_ROUTE} from "../../../../http/routes";
import useUserContext from "../../../../hooks/useUserContext";

const LoginForm = () => {
    const user = useUserContext();
    const {handleSubmit, watch, register} = useForm({mode: "onSubmit"});
    const navigate = useNavigate();
    const [error, setError] = useState(false);

    const onSubmit = async () => {
        const {email, password} = watch();
        try {
            await login(email, password);
            user?.setIsAuth(true);
            navigate(MAIN_ROUTE);
        } catch (e) {
            setError(true);
        }
    }
    return (
        <>
            <main className={cn("content", "login")}>
                <form onSubmit={handleSubmit(onSubmit)} className={cn("form")}>
                    <div className="field">
                        <input {...register("email")} type="email" placeholder="Почта" className={cn("input")}/>
                    </div>
                    <div className="field">
                        <input required {...register("password")} type="password" placeholder="Пароль"
                               className={cn("input")}/>
                    </div>
                    {error && <div className={cn("error")}>Неверное имя или пароль</div>}
                    <button type="submit" className={cn("submit-button")}>Войти</button>
                </form>
            </main>
        </>
    );
};

export default LoginForm;
