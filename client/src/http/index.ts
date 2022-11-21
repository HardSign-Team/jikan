import axios, {AxiosRequestConfig} from "axios";

const url = process.env.REACT_APP_API_URL;

const $host = axios.create({
        baseURL: url,
    }
)

const $authHost = axios.create({
    baseURL: url,
})

const authInterceptor = (config: AxiosRequestConfig) => {
    if (config.headers) {
        config.headers.authorization = `Bearer ${localStorage.getItem("refreshToken")}`
    }
    return config;
}

$authHost.interceptors.request.use(authInterceptor);

export {
    $host,
    $authHost
}