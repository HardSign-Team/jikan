import React from 'react';
import cn from "classnames"
import "./RegistrationForm.less"
import {useForm} from "react-hook-form";
import {register} from "../../../../http/userApi";
import useUserContext from "../../../../hooks/useUserContext";

interface RegistrationFormProps {
    onRegistration: () => void;
}

const RegistrationForm = ({onRegistration}: RegistrationFormProps) => {
    const user = useUserContext();
    const {register: registerForm, handleSubmit, watch, formState: {errors}} = useForm({mode: "onSubmit"});

    const onSubmit = async () => {
        const {email, password, name} = watch();
        try {
            const data = await register(email, password, name);
            console.log(data);
            user?.saveUserInfo(data);
            user?.setIsAuth(true);
            console.log(data);
            onRegistration();
        } catch (err) {
            console.error(err);
        }
    };

    const isPasswordSame = () => {
        const {password, repeatedPassword} = watch();
        return password === repeatedPassword;
    }
    return (
        <>
            <main className={cn("content")}>
                <form onSubmit={handleSubmit(onSubmit)} className={cn("form")}>
                    <div className="field">
                        <input placeholder="Почта" type="email" className={cn("input")} {...registerForm("email", {
                            required: true,
                            pattern: /^\S+@\S+$/i,
                            maxLength: 80
                        })} />
                    </div>
                    <div className="field">
                        <input placeholder="Имя" type="text" className={cn("input")} {...registerForm("name", {
                            required: true,
                            maxLength: 30,
                            minLength: 3,
                        })}/>
                        {errors.name?.type === "required" && <span className={cn("error")}>Требуется ввести имя</span>}
                        {errors.name?.type === "minLength" &&
                        <span className={cn("error")}>Имя должно быть длиннее 2 символов</span>}
                        {errors.name?.type === "maxLength" &&
                        <span className={cn("error")}>Имя должно быть короче 30 символов</span>}
                    </div>
                    <div className="field">
                        <input placeholder="Пароль" type="password"
                               className={cn("input")} {...registerForm("password", {
                            required: true,
                            maxLength: 80,
                            minLength: 8,
                        })}/>
                        {errors.password?.type === "required" &&
                        <span className={cn("error")}>Требуется ввести пароль</span>}
                        {errors.password?.type === "maxLength" &&
                        <span className={cn("error")}>Пароль должно быть короче 80 символов</span>}
                        {errors.password?.type === "minLength" &&
                        <span className={cn("error")}>Пароль должен быть длиннее 8 символов</span>}
                    </div>
                    <div className="field">
                        <input placeholder="Повторите пароль" type="password"
                               className={cn("input")} {...registerForm("repeatedPassword", {
                            validate: isPasswordSame,
                        })}/>
                        {errors.repeatedPassword?.type === "validate" &&
                        <span className={cn("error")}>Пароли не совпадают</span>}
                    </div>
                    <button type="submit" className={cn("submit-button")}>Создать</button>
                </form>
            </main>
        </>
    );
};

export default RegistrationForm;
