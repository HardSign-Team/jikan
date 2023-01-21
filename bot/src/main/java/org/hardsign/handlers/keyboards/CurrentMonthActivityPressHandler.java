package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.handlers.keyboards.abstracts.PeriodActivityPressHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.DateRange;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.YearMonth;

public class CurrentMonthActivityPressHandler extends PeriodActivityPressHandler {

    private final TimeFormatter timeFormatter;
    private final TimezoneHelper timezoneHelper;

    public CurrentMonthActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            TimeFormatter timeFormatter,
            TimezoneHelper timezoneHelper) {
        super(bot, jikanApiClient);
        this.timeFormatter = timeFormatter;
        this.timezoneHelper = timezoneHelper;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CURRENT_MONTH_STATISTICS.getName();
    }

    @NotNull
    @Override
    protected DateRange getDateRange(Update update) {
        var date = Instant.ofEpochSecond(update.message().date());
        var zone = timezoneHelper.getZone(update.message().location());

        var yearMonth = YearMonth.from(date.atZone(zone));
        var from = yearMonth.atDay(1).atStartOfDay(zone).toInstant();
        var to = yearMonth.atEndOfMonth().atStartOfDay(zone).toInstant();

        return new DateRange(from, to);
    }

    @Override
    protected String getTextMessage(ActivityDto activity, Duration duration) {
        var nameText = activity.getName();
        var durationText = timeFormatter.format(duration);
        return "За текущий месяц времени потрачено на активность '" + nameText + "': " + durationText;
    }
}
