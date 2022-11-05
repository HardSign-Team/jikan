package org.hardsign.services.settings;

import org.hardsign.models.settings.BotSettings;

public interface EnvironmentSettingsParser {
    BotSettings parse() throws Exception;
}

