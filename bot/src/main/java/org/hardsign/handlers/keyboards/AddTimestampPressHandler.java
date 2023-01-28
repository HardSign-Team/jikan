package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.Hints;

public class AddTimestampPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public AddTimestampPressHandler(
            TelegramBot bot,
            UserStateService userStateService) {
        this.bot = bot;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ADD_TIMESTAMP.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activity = context.getActivity();

        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        userStateService.setState(user, State.AddTimestampDateRange);
        context.setState(State.AddTimestampDateRange);

        var text = "Укажите период фиксации. Обратите внимание, что он не должен пересекаться с другими фиксациями.\n" +
                "Правильный формат:\n" +
                Hints.DATE_FORMAT_HINT + "\n" +
                Hints.DATE_RANGE_FORMAT_HINT;
        sendDefaultMenuMessage(bot, context, chatId, text);
    }
}
