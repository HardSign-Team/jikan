package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.OkHttpClient;
import org.hardsign.clients.JikanApiClientImpl;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            var okClient = new OkHttpClient();
            var jikanClient = new JikanApiClientImpl(okClient);
            var activities = jikanClient.activities().getAll();
            System.out.println(activities.getValue()[0].getName());
            //Run();
        } catch (Exception e) {
            System.out.println("Fatal error. Error: " + e.getMessage());
        }
    }

    private static void Run() throws Exception {
        var environment = getEnvironment();
        var token = getBotToken(environment);
        var bot = new TelegramBot(token);
        var updateListener = new UpdateListenerImpl(bot);
        bot.setUpdatesListener(updateListener);
    }

    private static String getBotToken(@NotNull Dotenv environment) {
        return environment.get("TELEGRAM_BOT_TOKEN");
    }

    private static Dotenv getEnvironment() throws Exception {
        var resource = Main.class.getClassLoader().getResource("env.properties");
        if (resource == null)
            throw new Exception("Resources not found. Please, create 'env.properties' file in resources.");
        var envProperties = new File(resource.getPath());
        return Dotenv
                .configure()
                .directory(envProperties.getParent())
                .filename(envProperties.getName())
                .load();
    }
}