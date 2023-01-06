package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.DateRange;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityTotalTimeDto;
import org.hardsign.models.activities.requests.GetActivityTotalTimeRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.State;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;

import java.time.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomDateInputHandler extends BaseUpdateHandler implements InputHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final TimeFormatter timeFormatter;
    private final TimezoneHelper timezoneHelper;
    private static final Pattern datePattern = Pattern.compile(
            "(?<day>\\d{1,2})\\.(?<month>\\d{1,2})\\.(?<year>\\d{4})" +
                    "\\s*((?<hours>\\d{2}):(?<minute>\\d{2}):(?<second>\\d{2}))?");

    public static final String DATE_FORMAT_HINT = "dd.mm.yyyy (hh:mm:ss)";
    public static final String DATE_RANGE_FORMAT_HINT = "date-date";
    public CustomDateInputHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService,
            TimeFormatter timeFormatter,
            TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
        this.timeFormatter = timeFormatter;
        this.timezoneHelper = timezoneHelper;
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
        if (dateRange.isEmpty()) {
            var text = getIncorrectFormatText();
            var keyboard = KeyboardFactory.createBackButtonMenu();
            bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
            return;
        }

        userStateService.setState(user, State.None);
        context.setState(State.None);

        var totalTime = getTotalTime(context, activity, dateRange.get());

        sendMessage(context, update.message().text(), activity, chatId, totalTime);
    }

    private String getIncorrectFormatText() {
        return "Неверный формат. Попробуйте еще раз.\n" +
                "Правильный формат: " + "\n" +
                DATE_FORMAT_HINT + "\n" +
                DATE_RANGE_FORMAT_HINT;
    }

    private ActivityTotalTimeDto getTotalTime(UpdateContext context, ActivityDto activity, DateRange dateRange)
            throws Exception {
        var apiRequest = new GetActivityTotalTimeRequest(activity.getId(), dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(apiRequest, context.getMeta());
        return jikanApiClient.activities().getTotalTime(botRequest).getValueOrThrow();
    }

    private Optional<DateRange> getDateRange(Update update) {
        var text = update.message().text();
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        var parts = text.split("-");

        if (parts.length != 2) {
            return Optional.empty();
        }

        var zone = timezoneHelper.getZone(update.message().location());
        var p = Arrays.stream(parts)
                .map(this::parseDate)
                .map(x -> x.map(y -> y.atZone(zone).toInstant()))
                .collect(Collectors.toList());

        var from = p.get(0);
        var to = p.get(1);
        if (from.isEmpty() || to.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new DateRange(from.get(), to.get()));
    }

    private Optional<LocalDateTime> parseDate(String dateText) {
        var matcher = datePattern.matcher(dateText);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        var date = parseLocalDate(matcher);
        var time = parseLocalTime(matcher);

        if (date.isEmpty() || time.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(LocalDateTime.of(date.get(), time.get()));
    }

    private Optional<LocalDate> parseLocalDate(Matcher matcher) {
        try {
            var day = parseGroupInt(matcher,"day");
            var month = parseGroupInt(matcher,"month");
            var year = parseGroupInt(matcher,"year");
            return Optional.of(LocalDate.of(year, month, day));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        }
    }

    private Optional<LocalTime> parseLocalTime(Matcher matcher) {
        try {
            var hour = parseGroupInt(matcher, "hour");
            var minute = parseGroupInt(matcher, "minutes");
            var second = parseGroupInt(matcher, "seconds");
            return Optional.of(LocalTime.of(hour, minute, second));
        } catch (NumberFormatException ignored) {
            return Optional.empty();
        } catch (IllegalArgumentException ignored) {
            return Optional.of(LocalTime.of(0, 0));
        }
    }

    private int parseGroupInt(Matcher matcher, String group) {
        return Integer.parseInt(matcher.group(group));
    }

    private void sendMessage(
            UpdateContext context,
            String input,
            ActivityDto activity,
            Long chatId,
            ActivityTotalTimeDto totalTime) {
        var duration = Duration.ofSeconds(totalTime.getDurationSec());
        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = "За период " +
                input +
                " времени потрачено на активность '"
                + activity.getName()
                + "': " + timeFormatter.format(duration);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    @Override
    protected State requiredState() {
        return State.SelectCustomDateRangeStatistics;
    }
}
