package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.TimestampsService;
import org.hardsign.services.users.UserStateService;

public class AcceptDeleteTimestampPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final TimestampsService timestampsService;
    private final UserStateService userStateService;

    public AcceptDeleteTimestampPressHandler(
            TelegramBot bot,
            TimestampsService timestampsService,
            UserStateService userStateService) {

        this.bot = bot;
        this.timestampsService = timestampsService;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACCEPT_DELETE.getName();
    }

    @Override
    protected State requiredState() {
        return State.DeleteTimestampConfirmation;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var state = userStateService.getState(user);
        var data = state.getStateData();

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        var timestampId = data.getTimestampId();
        if (timestampId == 0) {
            handleNotFoundTimestamp(bot, chatId, context);
            return;
        }

        var timestamp = timestampsService.findTimestamp(timestampId, context.getMeta()).orElse(null);
        if (timestamp == null) {
            handleNotFoundTimestamp(bot, chatId, context);
            return;
        }

        timestampsService.deleteTimestamp(timestamp.getId(), context.getMeta());
        if (context.isActiveTimestamp(timestampId)) {
            context.setActiveTimestamp(null);
        }

        sendDefaultMenuMessage(bot, context, chatId, "Вы удалили фиксацию " + Emoji.Pensive.value());
    }
}
