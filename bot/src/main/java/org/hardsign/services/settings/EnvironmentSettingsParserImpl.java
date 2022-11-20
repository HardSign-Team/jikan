package org.hardsign.services.settings;

import io.github.cdimascio.dotenv.Dotenv;
import org.hardsign.models.settings.BotDatabaseSettings;
import org.hardsign.models.settings.BotSettings;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class EnvironmentSettingsParserImpl implements EnvironmentSettingsParser {

    public EnvironmentSettingsParserImpl(URL resourceUrl) {
        initEnvironment(resourceUrl);
    }

    @Override
    public BotSettings parse() throws Exception {
        var databaseSettings = BotDatabaseSettings.builder()
                .user(getOrNull("POSTGRESQL_USER"))
                .password(getOrNull("POSTGRESQL_PASSWORD"))
                .databaseName(getOrNull("POSTGRESQL_DBNAME"))
                .host(getOrNull("POSTGRESQL_HOST"))
                .port(getOrNull("POSTGRESQL_PORT"))
                .databaseUrl(getOrNull("DATABASE_URL"))
                .build();

        return BotSettings.builder()
                .botTelegramToken(getOrThrow("TELEGRAM_BOT_TOKEN"))
                .botLogin(getOrThrow("JIKAN_BOT_LOGIN"))
                .botPassword(getOrThrow("JIKAN_BOT_PASSWORD"))
                .apiUrl(getOrThrow("JIKAN_API_URL"))
                .accessTokenLifeTime(getMinutes(getOrDefault("JIKAN_ACCESS_TOKEN_LIFETIME", "60")))
                .database(databaseSettings)
                .build();
    }

    private static Duration getMinutes(String jikan_access_token_lifetime) {
        return Duration.ofMinutes(Long.parseLong(jikan_access_token_lifetime));
    }

    private String getOrThrow(String key) throws Exception {
        var result = System.getProperty(key);
        if (result == null)
            throw new Exception(key + " not found in environment.");
        return result;
    }

    private String getOrNull(String key) {
        return System.getProperty(key);
    }

    private String getOrDefault(String key, String def){
        return System.getProperty(key, def);
    }

    private void initEnvironment(URL resourceUrl) {
        if (resourceUrl == null)
        {
            Dotenv
                    .configure()
                    .systemProperties()
                    .load();
        }

        var dotenv = loadFromFile(resourceUrl);
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));
    }

    private Dotenv loadFromFile(URL resourceUrl){
        var envProperties = new File(resourceUrl.getPath());
        return Dotenv
                .configure()
                .directory(envProperties.getParent())
                .filename(envProperties.getName())
                .load();
    }
}
