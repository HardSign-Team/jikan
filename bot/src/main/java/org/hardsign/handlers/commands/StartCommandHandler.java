package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.State;
import org.hardsign.models.users.requests.CreateUserRequest;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.services.users.UserStateService;

import java.util.UUID;

public class StartCommandHandler extends BaseTextUpdateHandler implements CommandHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public StartCommandHandler(TelegramBot bot, JikanApiClient jikanApiClient, UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return "/start";
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        if (!context.getState().isDefault()) {
            userStateService.setState(user, State.None);
            context.setState(State.None);
        }

        var chatId = update.message().chat().id();
        if (!context.isRegistered()) {
            var apiUser = registerUser(context.getMeta());
            context.setRegistered(true);
            context.setUser(apiUser);
        }

        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, "Выберите действие").replyMarkup(keyboard));
    }

    @Override
    protected boolean shouldBeRegistered() {
        return false;
    }

    @Override
    protected boolean shouldRequireState() {
        return false;
    }

    public UserDto registerUser(TelegramUserMeta meta) throws Exception {
        var name = meta.getLogin();
        var login = Long.toString(meta.getId());
        var password = UUID.randomUUID().toString();
        var request = new CreateUserRequest(name, login, password);
        return jikanApiClient.users().create(request).getValueOrThrow();
    }
}
