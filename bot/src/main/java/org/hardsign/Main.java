package org.hardsign;

import okhttp3.OkHttpClient;
import org.hardsign.clients.JikanApiClientImpl;
import org.hardsign.factories.HibernateSessionFactoryFactory;
import org.hardsign.models.settings.BotSettings;
import org.hardsign.repositories.UserStateRepositoryImpl;
import org.hardsign.services.BotExceptionHandler;
import org.hardsign.services.LoggingTelegramBotDecorator;
import org.hardsign.services.auth.AuthorizerImpl;
import org.hardsign.services.settings.EnvironmentSettingsParserImpl;
import org.hardsign.services.users.UserStateServiceImpl;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            Run();
        } catch (Exception e) {
            System.out.println("Fatal error. Error: " + e);
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
        var bot = new LoggingTelegramBotDecorator(token, Logger.getLogger(Main.class.getName()));
        var updateListener = new UpdateListenerImpl(jikanApiClient, bot, userStateService);
        var exceptionHandler = new BotExceptionHandler();
        bot.setUpdatesListener(updateListener, exceptionHandler);
    }



    @Nullable
    private static URL getResourceUrl() {
        return Main.class.getClassLoader().getResource("env.properties");
    }
}