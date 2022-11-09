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
    private String baseUrlHost;
    private String baseUrlPort;
    private String botTelegramToken;
    private BotDatabaseSettings database;
}
