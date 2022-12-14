package org.hardsign.services.settings;

import io.github.cdimascio.dotenv.Dotenv;
import org.hardsign.models.settings.BotDatabaseSettings;
import org.hardsign.models.settings.BotSettings;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class EnvironmentSettingsParserImpl implements EnvironmentSettingsParser {

    private final Dotenv environment;

    public EnvironmentSettingsParserImpl(URL resourceUrl) {
        this.environment = getEnvironment(resourceUrl);
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
                .apiTimeout(getSeconds(getOrDefault("JIKAN_API_TIMEOUT", "30")))
                .database(databaseSettings)
                .build();
    }

    private static Duration getMinutes(String minutes) {
        return Duration.ofMinutes(Long.parseLong(minutes));
    }

    private static Duration getSeconds(String seconds) {
        return Duration.ofSeconds(Long.parseLong(seconds));
    }

    private String getOrThrow(String key) throws Exception {
        var result = environment.get(key);
        if (result == null)
            throw new Exception(key + " not found in environment.");
        return result;
    }

    private String getOrNull(String key) {
        return environment.get(key);
    }

    private String getOrDefault(String key, String def){
        return environment.get(key, def);
    }

    private Dotenv getEnvironment(URL resourceUrl) {
        if (resourceUrl == null)
            return Dotenv
                    .configure()
                    .ignoreIfMissing()
                    .load();
        return loadFromFile(resourceUrl);
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
