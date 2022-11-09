package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import okhttp3.OkHttpClient;
import org.hardsign.clients.JikanApiClientImpl;
import org.hardsign.factories.HibernateSessionFactoryFactory;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.repositories.UserStateRepositoryImpl;
import org.hardsign.services.auth.AuthorizerImpl;
import org.hardsign.services.settings.EnvironmentSettingsParserImpl;
import org.hardsign.services.users.UserStateServiceImpl;
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

        var sessionFactory = HibernateSessionFactoryFactory.create(settings.getDatabase()); // todo: (tebaikin) 07.11.2022 should dispose
        var userStateRepository = new UserStateRepositoryImpl(sessionFactory);
        var userStateService = new UserStateServiceImpl(userStateRepository);

        var okClient = new OkHttpClient();
        var authorizer = new AuthorizerImpl(okClient, settingsProvider);
        authorizer.init();

        var jikanApiClient = new JikanApiClientImpl(okClient, authorizer, settingsProvider);

        var token = settings.getBotTelegramToken();
        var bot = new TelegramBot(token);
        var updateListener = new UpdateListenerImpl(jikanApiClient, bot, userStateService);
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