package org.hardsign.services.updateHandlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.UserState;
import org.hardsign.models.ButtonNames;
import org.hardsign.services.updateHandlers.BaseTextUpdateHandler;
import org.hardsign.services.users.UserStateService;

public class CreateActivityCommandHandler extends BaseTextUpdateHandler implements CommandHandler {
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CreateActivityCommandHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CREATE_ACTIVITY.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) {
        userStateService.setState(user, UserState.CreateActivityName);
        var chatId = update.message().chat().id();
        bot.execute(new SendMessage(chatId, "Напишите название для активности")
                            .replyMarkup(new ReplyKeyboardRemove()));
    }
}
