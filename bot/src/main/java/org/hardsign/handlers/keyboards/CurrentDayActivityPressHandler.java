package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.DateRange;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

public class CurrentDayActivityPressHandler extends PeriodActivityPressHandler {

    private final TimeFormatter timeFormatter;
    private final TimezoneHelper timezoneHelper;

    public CurrentDayActivityPressHandler(
            TelegramBot bot, JikanApiClient jikanApiClient,
            TimeFormatter timeFormatter,
            TimezoneHelper timezoneHelper) {
        super(bot, jikanApiClient);
        this.timeFormatter = timeFormatter;
        this.timezoneHelper = timezoneHelper;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CURRENT_DAY_STATISTICS.getName();
    }

    @NotNull
    @Override
    protected DateRange getDateRange(Update update) {
        var utc = timezoneHelper.getUtcZone();
        var date = Instant.ofEpochSecond(update.message().date());
        var zone = timezoneHelper.getZone(update.message().location());

        var atZone = date.atZone(zone);

        var from = atZone.toLocalDate().atStartOfDay(utc).toInstant();
        var to = atZone.toLocalDate().atTime(23, 59, 59).atZone(utc).toInstant();

        return new DateRange(from, to);
    }

    @Override
    protected String getTextMessage(ActivityDto activity, Duration duration) {
        var nameText = activity.getName();
        var durationText = timeFormatter.format(duration);
        return "За сегодня месяц времени потрачено на активность '" + nameText + "': " + durationText;
    }
}
