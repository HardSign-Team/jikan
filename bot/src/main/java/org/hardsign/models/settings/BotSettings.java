package org.hardsign.models.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotSettings {
    private String botLogin;
    private String botPassword;
    private String baseUrlHost;
    private String baseUrlPort;
    private String botTelegramToken;
    private BotDatabaseSettings database;
}
