package com.hardsign.server.services.time;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeProviderServiceImpl implements TimeProviderService {
    @Override
    public Date getCurrentDate() {
        return new Date();
    }
}
