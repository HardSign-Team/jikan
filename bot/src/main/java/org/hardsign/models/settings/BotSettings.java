package org.hardsign.models.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotSettings {
    private String botLogin;
    private String botPassword;
    private String baseUrl;
    private String botTelegramToken;
}
