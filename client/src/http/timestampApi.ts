import {$authHost} from "./index";
import {TimestampModel} from "../models/ActivityFullInfo";

export const getAllTimestamps = async (activityId: number) => {
    try {
        const {
            data
        }: { data: TimestampModel[] } = await $authHost.get("/api/timestamps/getAll/" + activityId);
        return data;
    } catch (e: any) {
        console.error(e.message);
    }
}

export const getLastTimestampByActivityId = async (activityId: number) => {
    try {
        const {
            data
        }: { data: TimestampModel } = await $authHost.get(`/api/timestamps/newest/${activityId}`);
        return data;
    } catch (e: any) {
        console.error(e.message);
    }
}

export const startActivity = async (activityId: number) => {
    try {
        const {
            data
        }: { data: TimestampModel } = await $authHost.post(`/api/timestamps/start`, {activityId});
        return data;
    } catch (e: any) {
        console.error(e.message);
    }
}

export const stopActivity = async (activityId: number) => {
    try {
        const {
            data
        }: { data: TimestampModel } = await $authHost.post(`/api/timestamps/stop`, {activityId});
        return data;
    } catch (e: any) {
        console.error(e.message);
    }
}

export const deleteActivity = async (activityId: number) => {
    try {
        await $authHost.post(`/api/timestamps/stop`, {activityId});
    } catch (e: any) {
        console.error(e.message);
    }
}
