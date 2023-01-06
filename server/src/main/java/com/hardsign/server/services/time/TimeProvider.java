package com.hardsign.server.services.time;

import java.time.Instant;

public interface TimeProvider {
    Instant now();
}

