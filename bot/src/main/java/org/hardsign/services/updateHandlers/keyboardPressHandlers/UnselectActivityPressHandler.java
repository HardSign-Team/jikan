package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.services.updateHandlers.BaseTextUpdateHandler;
import org.hardsign.services.users.UserStateService;

public class UnselectActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public UnselectActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        if (context.getActivityId() == 0) {
            handleNoActivity(context, chatId);
            return;
        }

        userStateService.setActivity(user, 0);
        context.setActivityId(0);

        var text = "Вы убрали текущую активность. Лентяйкин! :)";
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void handleNoActivity(UpdateContext context, Long chatId) throws Exception {
        var text = "Вы еще ничем не занимались. Лентяйкин! :)";
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.UNSELECT_ACTIVITY.getName();
    }
}

