package org.hardsign.services.settings;

import io.github.cdimascio.dotenv.Dotenv;
import org.hardsign.models.settings.BotSettings;

import java.io.File;
import java.net.URL;

public class EnvironmentSettingsParserImpl implements EnvironmentSettingsParser {
    private final Dotenv environment;

    public EnvironmentSettingsParserImpl(URL resourceUrl) {
        this.environment = getEnvironment(resourceUrl);
    }

    @Override
    public BotSettings parse() throws Exception {
        return BotSettings.builder()
                .botTelegramToken(getOrThrow("TELEGRAM_BOT_TOKEN"))
                .botLogin(getOrThrow("JIKAN_BOT_LOGIN"))
                .botPassword(getOrThrow("JIKAN_BOT_PASSWORD"))
                .baseUrlHost(getOrThrow("JIKAN_API_HOST"))
                .baseUrlPort(getOrThrow("JIKAN_API_PORT"))
                .build();
    }

    private String getOrThrow(String key) throws Exception {
        var result = environment.get(key);
        if (result == null)
            throw new Exception(key + " not found in environment.");
        return result;
    }

    private Dotenv getEnvironment(URL resourceUrl) {
        var envProperties = new File(resourceUrl.getPath());
        return Dotenv
                .configure()
                .directory(envProperties.getParent())
                .filename(envProperties.getName())
                .load();
    }
}
