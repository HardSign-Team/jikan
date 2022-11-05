package org.hardsign.services.updateHandlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.auth.TelegramUserAuthMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.services.updateHandlers.keyboardPressHandlers.ButtonNames;

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
    public void handle(Update update) {
        var chatId = update.message().chat().id();
        var replyMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(ButtonNames.ACTIVITIES.getName()));
        var request = new SendMessage(chatId, "Choose your variant or /start")
                .replyMarkup(replyMarkup);
        //sendMessage(request);
        var user = update.message().from();
        if (!user.isBot())
            registerUser(user);
    }

    private void registerUser(User user) {
        var meta = new TelegramUserAuthMeta(user.id(), user.username(), user.firstName());
        var findUserByLoginRequest = new FindUserByLoginRequest(Long.toString(meta.getId()));
        var apiUser = jikanApiClient.users().findByLogin(new BotRequest<>(findUserByLoginRequest, meta));
        if (apiUser.isSuccess() && apiUser.getCode() == 200) // todo: (tebaikin) 05.11.2022 refactor
            return;

        try {
            createUser(meta);
            LOGGER.info("Create user id = " + user.id() + " and username = " + user.username());
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }
    }

    public void createUser(TelegramUserAuthMeta meta) throws Exception {
        var name = meta.getLogin();
        var login = Long.toString(meta.getId());
        var password = UUID.randomUUID().toString();
        var request = new CreateUserRequest(name, login, password);
        jikanApiClient.users().create(new BotRequest<>(request, meta)).getValueOrThrow().orElseThrow();
    }
}
