package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import okhttp3.OkHttpClient;
import org.hardsign.clients.JikanApiClientImpl;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.services.AuthorizerImpl;
import org.hardsign.services.EnvironmentSettingsParserImpl;
import org.hardsign.services.UpdateListenerImpl;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        try {
            Run();
        } catch (Exception e) {
            System.out.println("Fatal error. Error: " + e.getMessage());
        }
    }

    private static void Run() throws Exception {
        var resourceUrl = getResourceUrl();
        var parser = new EnvironmentSettingsParserImpl(resourceUrl);
        var settings = parser.parse();
        Supplier<BotSettings> settingsProvider = () -> settings;

        var okClient = new OkHttpClient();
        var authorizer = new AuthorizerImpl(okClient, settingsProvider);
        authorizer.init();

        var jikanApiClient = new JikanApiClientImpl(okClient, authorizer, settingsProvider);

        var token = settings.getBotTelegramToken();
        var bot = new TelegramBot(token);
        var updateListener = new UpdateListenerImpl(jikanApiClient, bot);
        bot.setUpdatesListener(updateListener);
    }

    @NotNull
    private static URL getResourceUrl() throws Exception {
        var resource = Main.class.getClassLoader().getResource("env.properties");
        if (resource == null)
            throw new Exception("Resources not found. Please, create 'env.properties' file in resources.");
        return resource;
    }
}