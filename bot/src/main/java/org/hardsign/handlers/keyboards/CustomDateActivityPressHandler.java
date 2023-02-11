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
import org.hardsign.utils.Hints;

public class CustomDateActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final UserStateService userStateService;

    public CustomDateActivityPressHandler(TelegramBot bot, UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CUSTOM_DATE_STATISTICS.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        userStateService.with(context).setState(user, State.SelectCustomDateRangeStatistics);

        var text = "Укажите период.\n" +
                "Правильный формат:\n" +
                Hints.DATE_FORMAT_HINT + "\n" +
                Hints.DATE_RANGE_FORMAT_HINT;
        var keyboard = KeyboardFactory.createBackButtonMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
