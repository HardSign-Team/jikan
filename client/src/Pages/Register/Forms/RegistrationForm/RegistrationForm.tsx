import React from 'react';
import cn from "classnames"
import "./RegistrationForm.less"
import {useForm} from "react-hook-form";
import {register} from "../../../../http/userApi";
import useUserContext from "../../../../hooks/useUserContext";

interface RegistrationFormProps {
    onRegistration: () => void;
}

type ValidationErrorMessage = {
    [field: string]: {
        [k: string]: string;
    }
}
const validationErrorMessages: ValidationErrorMessage = {
    login: {
        required: "Требуется ввести логин",
        minLength: "Имя должно быть длиннее 5 символов",
        maxLength: "Имя должно быть короче 30 символов",
    },
    type: {
        required: "Требуется ввести имя",
        minLength: "Имя должно быть длиннее 2 символов",
        maxLength: "Имя должно быть короче 30 символов",
    },
    password: {
        required: "Требуется ввести пароль",
        maxLength: "Пароль должен быть короче 80 символов",
        minLength: "Пароль должен быть длиннее 8 символов",
    }
}

const RegistrationForm = ({onRegistration}: RegistrationFormProps) => {
    const user = useUserContext();
    const {register: registerForm, handleSubmit, watch, formState: {errors}} = useForm({mode: "onSubmit"});

    const onSubmit = async () => {
        const {login, password, name} = watch();
        try {
            const data = await register(login, password, name);
            user?.saveUserInfo(data);
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
                        <input placeholder="Логин" type="login" className={cn("input")} {...registerForm("login", {
                            required: true,
                            minLength: 5,
                            maxLength: 30
                        })} />
                        {errors.login?.type &&
                        <span className={cn("error")}>
                            {validationErrorMessages.login[errors.login.type.toString()]}
                        </span>
                        }
                    </div>
                    <div className="field">
                        <input placeholder="Имя" type="text" className={cn("input")} {...registerForm("name", {
                            required: true,
                            maxLength: 30,
                            minLength: 3,
                        })}/>
                        {errors.name?.type &&
                        <span className={cn("error")}>
                            {validationErrorMessages.type[errors.name.type.toString()]}
                        </span>
                        }
                    </div>
                    <div className="field">
                        <input placeholder="Пароль" type="password"
                               className={cn("input")} {...registerForm("password", {
                            required: true,
                            maxLength: 80,
                            minLength: 8,
                        })}/>
                        {errors.password?.type &&
                        <span className={cn("error")}>
                            {validationErrorMessages.password[errors.password.type.toString()]}
                        </span>
                        }
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
