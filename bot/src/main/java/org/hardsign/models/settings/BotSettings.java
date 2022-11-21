package org.hardsign.models.settings;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class BotSettings {
    private String botLogin;
    private String botPassword;
    private Duration accessTokenLifeTime;
    private Duration emergencyAuthorizationPeriod;
    private Duration apiTimeout;
    private String apiUrl;
    private String botTelegramToken;
    private BotDatabaseSettings database;
}
