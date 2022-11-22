import {$authHost} from "./index";
import {ActivityModel} from "../models/Activity";
import {getNewAccessToken} from "./userApi";
import {ActivityFullInfo} from "../models/ActivityFullInfo";

export const createActivity = async (activityName: string) => {
    try {
        const {data: {id, userId, name}}: { data: ActivityModel } = await _createActivity(activityName);
        return {id, userId, name};
    } catch (e: any) {
        if (e.response.status === 401) {
            await getNewAccessToken();
            const {data: {id, userId, name}}: { data: ActivityModel } = await _createActivity(activityName);
            return {id, userId, name};
        }
    }
}

export const getAllActivities = async () => {
    try {
        const {data}: { data: ActivityFullInfo[] } = await $authHost.get("/api/activities/overview/all");
        return data;
    } catch (e: any) {
        if (e.response?.status === 401) {
            await getNewAccessToken();
            const {data}: { data: ActivityFullInfo[] } = await $authHost.get("/api/activities/overview/all");
            return data;
        }
    }
}

const _createActivity = async (activityName: string) => {
    return await $authHost.post("api/activities/create", {
        name: activityName
    });
}