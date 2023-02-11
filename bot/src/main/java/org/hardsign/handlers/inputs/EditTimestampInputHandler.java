package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.DateRange;
import org.hardsign.models.HttpCodes;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.EditTimestampRequest;
import org.hardsign.models.timestamps.requests.GetTimestampByIdRequest;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.DateParserFromUpdate;
import org.hardsign.utils.MessagesHelper;

public class EditTimestampInputHandler extends BaseUpdateHandler implements InputHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final DateParserFromUpdate dateParser;

    public EditTimestampInputHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService,
            DateParserFromUpdate dateParser) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
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

        var timestamp = requestTimestamp(timestampId, context.getMeta());
        if (timestamp == null) {
            handleTimestampNotFound(context, chatId);
            return;
        }

        var dateRange = dateParser.parseDateRange(update);
        if (dateRange.isEmpty()) {
            sendIncorrectFormatMessage(chatId, context);
            return;
        }

        var result = editTimestamp(timestampId, dateRange.get(), context.getMeta());

        if (HttpCodes.Conflict.is(result.getCode())) {
            sendConflictMessage(chatId, context);
            return;
        }

        result.ensureSuccess();

        sendSuccessMessage(update, context, chatId);
    }

    private JikanResponse<TimestampDto> editTimestamp(long timestampId, DateRange dateRange, TelegramUserMeta meta) {
        var request = new EditTimestampRequest(timestampId, dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(request, meta);
        return jikanApiClient.timestamps().edit(botRequest);
    }

    private void handleTimestampNotFound(UpdateContext context, Long chatId) {
        sendDefaultMenuMessage(bot, context, chatId, "Фиксация не найдена");
    }

    private TimestampDto requestTimestamp(long timestampId, TelegramUserMeta meta) throws Exception {
        var request = new GetTimestampByIdRequest(timestampId);
        var botRequest = new BotRequest<>(request, meta);
        var response = jikanApiClient.timestamps().getById(botRequest);
        if (response.notFound()) {
            return null;
        }
        return response.getValueOrThrow();
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
