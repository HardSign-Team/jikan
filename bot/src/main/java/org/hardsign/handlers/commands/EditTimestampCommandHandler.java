package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.commands.abstracts.BaseIdCommandsHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.GetTimestampByIdRequest;
import org.hardsign.models.users.State;
import org.hardsign.models.users.StateData;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;
import org.hardsign.utils.ValidationHelper;
import org.jetbrains.annotations.Nullable;

import java.time.ZoneId;

public class EditTimestampCommandHandler extends BaseIdCommandsHandler implements CommandHandler {
    private static final String commandPrefix = "/editt_";
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final TimeFormatter formatter;
    private final TimezoneHelper timezoneHelper;

    public EditTimestampCommandHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService,
            TimeFormatter formatter,
            TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
        this.formatter = formatter;
        this.timezoneHelper = timezoneHelper;
    }

    public static String create(long timestampId) {
        return commandPrefix + timestampId;
    }

    @Override
    protected String getPrefix() {
        return commandPrefix;
    }

    @Override
    protected void handleInternal(User user, Long id, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        var timestamp = getTimestamp(id, context.getMeta());
        if (timestamp == null) {
            handleTimestampNotFoundError(chatId, context);
            return;
        }

        var activity = getActivity(timestamp.getActivityId(), context.getMeta());
        if (activity == null) {
            handleActivityNotFoundError(chatId, context);
            return;
        }

        if (!ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            handleNotOwnActivityError(chatId, context);
            return;
        }

        var zone = timezoneHelper.getZone(update.message().location());

        handleSuccess(user, context, chatId, timestamp, zone);
    }

    private void handleSuccess(User user, UpdateContext context, Long chatId, TimestampDto timestamp, ZoneId zone) {
        var patch = UserStatePatch.builder()
                .state(State.EditTimestamp)
                .stateData(StateData.fromTimestamp(timestamp.getId()))
                .build();
        userStateService.with(context).update(user, patch);

        var dateText = formatter.format(timestamp.getDateRange(zone));
        var text = "Введите новый период для фиксации " + TelegramUtils.bold(dateText);
        var keyboard = KeyboardFactory.createBackButtonMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleNotOwnActivityError(Long chatId, UpdateContext context) {
        sendDefaultMenuMessage(bot, context, chatId, "Активность для фиксции не принадлежит вам!");
    }

    private void handleActivityNotFoundError(Long chatId, UpdateContext context) {
        sendDefaultMenuMessage(bot, context, chatId, "Активность для фиксации не найдена о_О");
    }

    private void handleTimestampNotFoundError(Long chatId, UpdateContext context) {
        sendDefaultMenuMessage(bot, context, chatId, "Фиксация не найдена.");
    }

    @Nullable
    private ActivityDto getActivity(Long id, TelegramUserMeta meta) throws Exception {
        var botRequest = new BotRequest<>(new GetActivityByIdRequest(id), meta);
        var result = jikanApiClient.activities().getById(botRequest);
        if (result.notFound()) {
            return null;
        }
        result.ensureSuccess();
        return result.getValue().orElse(null);

    }

    @Nullable
    private TimestampDto getTimestamp(Long id, TelegramUserMeta meta) throws Exception {
        var botRequest = new BotRequest<>(new GetTimestampByIdRequest(id), meta);
        var result = jikanApiClient.timestamps().getById(botRequest);
        if (result.notFound()) {
            return null;
        }
        result.ensureSuccess();
        return result.getValue().orElse(null);
    }
}
