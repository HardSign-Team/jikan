package org.hardsign.services.updateHandlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.requests.CreateUserRequest;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class StartCommandHandler implements CommandHandler {
    private static final Logger LOGGER = Logger.getLogger("StartCommandHandler");
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public StartCommandHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.getState().isDefault())
            return;

        if (!Objects.equals(update.message().text(), "/start"))
            return;

        var chatId = update.message().chat().id();

        if (!context.isRegistered()) {
            var registered = registerUser(user, context.getMeta());
            if (!registered) {
                var request = new SendMessage(chatId, "Произошла ошибка при создании аккаунта. Пожалуйста, попробуйте еще раз.");
                bot.execute(request);
                return;
            }
        }

        var replyMarkup = KeyboardFactory.createMainMenu(context, jikanApiClient);
        var request = new SendMessage(chatId, "Выберите действие")
                .replyMarkup(replyMarkup);
        bot.execute(request);
    }

    private boolean registerUser(User user, TelegramUserMeta meta) {
        try {
            createUser(meta);
            LOGGER.info("Create user id = " + user.id() + " and username = " + user.username());
            return true;
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return false;
        }
    }

    public void createUser(TelegramUserMeta meta) throws Exception {
        var name = meta.getLogin();
        var login = Long.toString(meta.getId());
        var password = UUID.randomUUID().toString();
        var request = new CreateUserRequest(name, login, password);
        jikanApiClient.users().create(new BotRequest<>(request, meta)).getValueOrThrow();
    }
}
