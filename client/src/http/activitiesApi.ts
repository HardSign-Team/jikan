import {$authHost} from "./index";
import {ActivityModel} from "../models/Activity";

export const createActivity = async (activityName: string) => {
    const { id, userId, name }: ActivityModel = await $authHost.post("api/activities/create", {
        name: activityName
    });
    console.log(id, userId, name);
}