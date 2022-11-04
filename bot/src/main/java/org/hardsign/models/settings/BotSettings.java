package org.hardsign.models.settings;

import lombok.Data;

@Data
public class BotSettings {
    private String botLogin;
    private String botPassword;
    private String baseUrl;
}
