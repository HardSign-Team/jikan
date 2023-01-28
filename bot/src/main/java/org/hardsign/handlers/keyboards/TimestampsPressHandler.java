package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.*;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.TimestampField;
import org.hardsign.models.timestamps.requests.FindTimestampsRequest;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static org.hardsign.utils.TelegramUtils.bold;

public class TimestampsPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final TimeFormatter formatter;
    private final TimezoneHelper timezoneHelper;

    public TimestampsPressHandler(TelegramBot bot, JikanApiClient jikanApiClient, TimeFormatter formatter, TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.formatter = formatter;
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

        var text = createText(activity, timestamps, zone);
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private TimestampDto[] getTimestamps(UpdateContext context, ActivityDto activity) throws Exception {
        var start = Instant.EPOCH;
        var end = Instant.now();
        var sort = List.of(new SortField<>(TimestampField.START, SortDirection.Descending));
        var request = new BotRequest<>(new FindTimestampsRequest(activity.getId(), start, end, 0, 15, sort), context.getMeta());
        var timestamps = jikanApiClient.timestamps().find(request).getValueOrThrow();

        return Arrays.stream(timestamps).sorted(this::compareAscending).toArray(TimestampDto[]::new);
    }

    @NotNull
    private String createText(ActivityDto activity, TimestampDto[] timestamps, ZoneId zone) {
        if (timestamps.length == 0) {
            return "Вы еще не фиксировали активность :)";
        }

        var sb = new StringBuilder();
        sb.append("Последние фиксации активности ").append(bold(activity.getName())).append(':').append(lineSeparator());

        for (var i = 0; i < timestamps.length; i++) {
            var str = createTimestampDescription(i + 1, timestamps[i], zone);
            sb.append(str).append(lineSeparator());
        }

        return sb.toString();
    }

    @NotNull
    private String createTimestampDescription(int position, TimestampDto timestamp, ZoneId zone) {
        var timestampSb = new StringBuilder();
        var start = formatter.format(timestamp.getStart().atZone(zone));
        var end = Optional.ofNullable(timestamp.getEnd()).map(x -> x.atZone(zone)).map(formatter::format).orElse("...");

        timestampSb.append(Emoji.Clock1).append(' ');
        timestampSb.append(position).append(". ").append(bold(start)).append(" — ").append(bold(end)).append(lineSeparator());
        timestampSb.append("Редактировать: ").append("/edit_").append(timestamp.getId()).append(lineSeparator());
        timestampSb.append("Удалить: ").append("/delt_").append(timestamp.getId()).append(lineSeparator());

        return timestampSb.toString();
    }

    private int compareAscending(TimestampDto x, TimestampDto y) {
        return x.getStart().compareTo(y.getStart());
    }
}
