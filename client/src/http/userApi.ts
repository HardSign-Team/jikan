import {$authHost, $host} from "./index";
import jwt_decode from "jwt-decode";
import {JwtTokens} from "../models/JwtTokens";
import {UserModel} from "../models/UserModel";

export const getNewAccessToken = async (): Promise<{ sub: string, name: string }> => {
    const {data: {accessToken, refreshToken}}: { data: JwtTokens } = await $host.post("api/auth/token", {
        refreshToken: `${localStorage.getItem("refreshToken")}`,
    });
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    return jwt_decode(accessToken);
}

export const login = async (login: string, password: string) => {
    const {data: {accessToken, refreshToken}}: { data: JwtTokens } = await $host.post("api/auth/login", {
        login,
        password
    });
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    return jwt_decode(accessToken);
}

export const register = async (login: string, password: string, name: string): Promise<UserModel> => {
    const {data: user} = await $host.post("api/users/", {login, password, name});
    return user;
}

export const getUser = async (login: string): Promise<UserModel> => {
    const {data: user} = await $authHost.get("api/users/login/" + login.replace("@", "%40"));
    return user;
}