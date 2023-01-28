package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.factories.TimestampsListFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.SortDirection;
import org.hardsign.models.SortField;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.TimestampField;
import org.hardsign.models.timestamps.requests.FindTimestampsRequest;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.TimezoneHelper;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

public class TimestampsPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final TimestampsListFactory timestampsListFactory;
    private final TimezoneHelper timezoneHelper;

    public TimestampsPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            TimestampsListFactory timestampsListFactory,
            TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.timestampsListFactory = timestampsListFactory;
        this.timezoneHelper = timezoneHelper;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.TIMESTAMPS.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var timestamps = getTimestamps(context, activity);

        var zone = timezoneHelper.getZone(update.message().location());

        var text = timestampsListFactory.create(activity, timestamps, zone);
        var keyboard = KeyboardFactory.createTimestampsMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private TimestampDto[] getTimestamps(UpdateContext context, ActivityDto activity) throws Exception {
        var start = Instant.EPOCH;
        var end = Instant.now();
        var sort = List.of(new SortField<>(TimestampField.START, SortDirection.Descending));
        var request = new BotRequest<>(new FindTimestampsRequest(activity.getId(), start, end, 0, 15, sort), context.getMeta());
        var timestamps = jikanApiClient.timestamps().find(request).getValueOrThrow();

        return Arrays.stream(timestamps).sorted(TimestampDto::compareAscending).toArray(TimestampDto[]::new);
    }
}
