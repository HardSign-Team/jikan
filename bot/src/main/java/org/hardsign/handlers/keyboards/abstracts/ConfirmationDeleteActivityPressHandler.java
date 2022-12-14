package org.hardsign.handlers.keyboards.abstracts;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.handlers.keyboards.KeyboardPressHandler;
import org.hardsign.services.users.UserStateService;

public abstract class ConfirmationDeleteActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    protected void clearState(User user, UserStateService userStateService, UpdateContext context) {
        var state = State.None;
        var patch = UserStatePatch.builder()
                .state(state)
                .deleteActivityId(0L)
                .build();
        userStateService.update(user, patch);
        context.setState(state);
    }

    protected void handleNotFoundActivity(
            TelegramBot bot,
            Long chatId,
            UpdateContext context) {
        var noActivityMessage = "Произошла ошибка! Не нашли выбранную активность. Попробуйте заново (по-братски)";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, noActivityMessage).replyMarkup(keyboard));
    }
}
