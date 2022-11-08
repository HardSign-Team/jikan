package com.hardsign.server.services.time;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class TimeProviderImpl implements TimeProvider {
    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now();
    }
}
