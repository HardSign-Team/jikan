import {$authHost, $host} from "./index";
import jwt_decode from "jwt-decode";
import {JwtTokens} from "../models/JwtTokens";
import {UserModel} from "../models/UserModel";

export const check = async () => {
    console.log(localStorage.getItem("accessToken"));
    const {data: {accessToken, refreshToken}}: { data: JwtTokens } = await $authHost.post("api/auth/token", {
        refreshToken: localStorage.getItem("accessToken"),
    });
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    return jwt_decode(accessToken);
}

export const login = async (email: string, password: string) => {
    const {data: {accessToken, refreshToken}}: { data: JwtTokens } = await $host.post("api/auth/login", {
        login: email,
        password
    });
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    return jwt_decode(accessToken);
}

export const register = async (email: string, password: string, name: string): Promise<UserModel> => {
    const {data: user} = await $host.post("api/users/", {login: email, password, name});
    return user;
}