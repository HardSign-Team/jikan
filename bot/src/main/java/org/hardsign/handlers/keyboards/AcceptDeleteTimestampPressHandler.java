package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.DeleteTimestampByIdRequest;
import org.hardsign.models.timestamps.requests.GetTimestampByIdRequest;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;

public class AcceptDeleteTimestampPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public AcceptDeleteTimestampPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {

        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
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
        var state = userStateService.getState(user);
        var data = state.getStateData();
        var timestampId = data.getTimestampId();

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        var chatId = update.message().chat().id();
        if (timestampId == 0) {
            handleNotFoundTimestamp(bot, chatId, context);
            return;
        }

        var timestamp = getTimestamp(context, timestampId);
        if (timestamp == null) {
            handleNotFoundTimestamp(bot, chatId, context);
            return;
        }

        deleteTimestamp(timestamp, context);

        var activeTimestamp = context.getActiveTimestamp();
        if (activeTimestamp != null && activeTimestamp.getId() == timestampId) {
            context.setActiveTimestamp(null);
        }

        sendDefaultMenuMessage(bot, context, chatId, "Вы удалили фиксацию " + Emoji.Pensive.value());
    }

    private void deleteTimestamp(TimestampDto timestamp, UpdateContext context) throws Exception {
        var request = new DeleteTimestampByIdRequest(timestamp.getId());
        var botRequest = new BotRequest<>(request, context.getMeta());
        jikanApiClient.timestamps().delete(botRequest).ensureSuccess();
    }

    private TimestampDto getTimestamp(UpdateContext context, long timestampId) throws Exception {
        var request = new GetTimestampByIdRequest(timestampId);
        var botRequest = new BotRequest<>(request, context.getMeta());
        var timestamp = jikanApiClient.timestamps().getById(botRequest);
        if (timestamp.notFound())
            return null;
        return timestamp.getValueOrThrow();
    }

    private void handleNotFoundTimestamp(TelegramBot bot, Long chatId, UpdateContext context) {
        sendDefaultMenuMessage(bot, context, chatId, "Фиксация не найдена :(");
    }
}
