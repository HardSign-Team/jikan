package org.hardsign.models.settings;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BotDatabaseSettings {
    private String user;
    private String password;
    private String databaseName;
    private String host;
    private String port;
    private String databaseUrl;
}
