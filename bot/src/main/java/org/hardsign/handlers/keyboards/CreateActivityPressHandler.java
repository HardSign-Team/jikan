package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.services.users.UserStateService;

public class CreateActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CreateActivityPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CREATE_ACTIVITY.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) {
        userStateService.with(context).setState(user, State.CreateActivityName);
        var chatId = update.message().chat().id();
        var text = "Напишите название для активности";
        var replyMarkup = KeyboardFactory.createBackButtonMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup));
    }
}
