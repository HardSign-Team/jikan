package com.hardsign.server.services.time;

import java.time.ZonedDateTime;

public interface TimeProvider {
    ZonedDateTime now();
}

