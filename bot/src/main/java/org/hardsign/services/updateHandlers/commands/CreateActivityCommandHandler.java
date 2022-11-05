package org.hardsign.services.updateHandlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.UserState;
import org.hardsign.models.ButtonNames;
import org.hardsign.services.users.UserStateService;

public class CreateActivityCommandHandler implements CommandHandler {
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CreateActivityCommandHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }
    @Override
    public void handle(Update update, UpdateContext context) {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        var text = update.message().text();
        if (!text.equals("/create_activity") && !text.equals(ButtonNames.CREATE_ACTIVITY.getName()))
            return;

        userStateService.setState(user, UserState.CreateActivityName);
        var chatId = update.message().chat().id();
        var replyKeyboard = new ReplyKeyboardRemove();
        bot.execute(new SendMessage(chatId, "Напишите название для активности").replyMarkup(replyKeyboard));
    }
}
