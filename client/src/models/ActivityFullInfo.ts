export interface ActivityFullInfo {
    id: number;
    userId: number;
    name: string;
    activeTimestamp: Nullable<TimestampModel>;
}

export interface TimestampModel {
    id: number;
    activityId: number;
    start: Nullable<ZonedDateTime>;
    end: Nullable<ZonedDateTime>;
}

export type ZonedDateTime = any;