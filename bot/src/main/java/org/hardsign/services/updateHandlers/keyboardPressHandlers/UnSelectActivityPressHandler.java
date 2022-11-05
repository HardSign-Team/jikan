package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.services.users.UserStateService;

import java.util.Objects;

public class UnSelectActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final UserStateService userStateService;

    public UnSelectActivityPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        if (!context.getState().isDefault())
            return;

        var message = update.message().text();
        if (!Objects.equals(message, ButtonNames.UNSELECT_ACTIVITY.getName()))
            return;

        var chatId = update.message().chat().id();

        if (context.getActivityId() == 0) {
            bot.execute(new SendMessage(chatId, "Вы еще ничем не занимались. Лентяйкин! :)"));
            return;
        }

        userStateService.setActivity(user, 0);

        bot.execute(new SendMessage(chatId, "Вы убрали текущую активность. Лентяйкин! :)"));
    }
}

