package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.DateRange;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityTotalTimeDto;
import org.hardsign.models.activities.requests.GetActivityTotalTimeRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.time.YearMonth;

public class CurrentMonthActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final TimeFormatter timeFormatter;
    private final TimezoneHelper timezoneHelper;

    public CurrentMonthActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            TimeFormatter timeFormatter,
            TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.timeFormatter = timeFormatter;
        this.timezoneHelper = timezoneHelper;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.CURRENT_MONTH_STATISTICS.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activity = context.getActivity();

        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var dateRange = getDateRange(update);

        var totalTime = getTotalTime(context, activity, dateRange);

        sendMessage(context, activity, chatId, totalTime);
    }

    private ActivityTotalTimeDto getTotalTime(UpdateContext context, ActivityDto activity, DateRange dateRange)
            throws Exception {
        var apiRequest = new GetActivityTotalTimeRequest(activity.getId(), dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(apiRequest, context.getMeta());
        return jikanApiClient.activities().getTotalTime(botRequest).getValueOrThrow();
    }

    @NotNull
    private DateRange getDateRange(Update update) {
        var utc = timezoneHelper.getUtcZone();
        var date = Instant.ofEpochSecond(update.message().date());
        var zone = timezoneHelper.getZone(update.message().location());

        var yearMonth = YearMonth.from(date.atZone(zone));
        var from = yearMonth.atDay(1).atStartOfDay(utc).toInstant();
        var to = yearMonth.atEndOfMonth().atStartOfDay(utc).toInstant();

        return new DateRange(from, to);
    }

    private void sendMessage(UpdateContext context, ActivityDto activity, Long chatId, ActivityTotalTimeDto totalTime) {
        var duration = Duration.ofSeconds(totalTime.getDurationSec());
        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = "За текущий месяц времени потрачено на активность '"
                + activity.getName()
                + "':"
                + timeFormatter.format(duration);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
