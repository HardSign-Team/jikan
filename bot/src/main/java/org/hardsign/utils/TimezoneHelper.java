package org.hardsign.utils;

import com.pengrad.telegrambot.model.Location;

import java.time.ZoneId;

public class TimezoneHelper {
    public ZoneId getZone(Location location) {
        return ZoneId.of("Asia/Yekaterinburg"); // todo: (tebaikin) 06.01.2023 should use location
    }

    public ZoneId getUtcZone() {
        return ZoneId.of("UTC");
    }
}
