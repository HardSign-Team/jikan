package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.timestamps.TimestampsService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.DateParserFromUpdate;
import org.hardsign.utils.MessagesHelper;

public class EditTimestampInputHandler extends BaseUpdateHandler implements InputHandler {

    private final TelegramBot bot;
    private final TimestampsService timestampsService;
    private final UserStateService userStateService;
    private final DateParserFromUpdate dateParser;

    public EditTimestampInputHandler(
            TelegramBot bot,
            TimestampsService timestampsService,
            UserStateService userStateService,
            DateParserFromUpdate dateParser) {
        this.bot = bot;
        this.timestampsService = timestampsService;
        this.userStateService = userStateService;
        this.dateParser = dateParser;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var state = userStateService.getState(user);
        var data = state.getStateData();
        var timestampId = data.getTimestampId();

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        if (timestampId == 0) {
            handleNoCurrentTimestamp(context, chatId);
            return;
        }

        var timestamp = timestampsService.getTimestamp(timestampId, context.getMeta()).orElse(null);
        if (timestamp == null) {
            handleTimestampNotFound(context, chatId);
            return;
        }

        var dateRange = dateParser.parseDateRange(update);
        if (dateRange.isEmpty()) {
            sendIncorrectFormatMessage(chatId, context);
            return;
        }

        var result = timestampsService.editTimestamp(timestampId, dateRange.get(), context.getMeta());
        if (result.isConflicted()) {
            sendConflictMessage(chatId, context);
            return;
        }

        sendSuccessMessage(update, context, chatId);
    }

    private void handleTimestampNotFound(UpdateContext context, Long chatId) {
        sendDefaultMenuMessage(bot, context, chatId, "Фиксация не найдена");
    }

    private void handleNoCurrentTimestamp(UpdateContext context, Long chatId) {
        sendDefaultMenuMessage(bot, context, chatId, "Фиксация не выбрана");
    }

    private void sendSuccessMessage(Update update, UpdateContext context, Long chatId) {
        var input = update.message().text();
        var text = "Фиксация успешно изменена на " + input;
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void sendConflictMessage(Long chatId, UpdateContext context) {
        var text = "Введенный период пересекается с другой фиксацией. Пожалуйста, измени период :o";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void sendIncorrectFormatMessage(Long chatId, UpdateContext context) {
        var text = MessagesHelper.createIncorrectDateRangeFormatMessage();
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected State requiredState() {
        return State.EditTimestamp;
    }
}
