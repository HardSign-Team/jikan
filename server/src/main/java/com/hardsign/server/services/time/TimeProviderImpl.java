package com.hardsign.server.services.time;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TimeProviderImpl implements TimeProvider {
    @Override
    public Instant now() {
        return Instant.now();
    }
}
