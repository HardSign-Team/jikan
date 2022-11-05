package org.hardsign.services;

import org.hardsign.models.settings.BotSettings;

public interface EnvironmentSettingsParser {
    BotSettings parse() throws Exception;
}

