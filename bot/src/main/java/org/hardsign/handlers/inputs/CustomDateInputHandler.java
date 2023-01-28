package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.DateRange;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityTotalTimeDto;
import org.hardsign.models.activities.requests.GetActivityTotalTimeRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.State;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.*;

import java.time.*;

public class CustomDateInputHandler extends BaseUpdateHandler implements InputHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final TimeFormatter timeFormatter;
    private final DateParserFromUpdate dateParser;

    public CustomDateInputHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService,
            TimeFormatter timeFormatter,
            DateParserFromUpdate dateParser) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
        this.timeFormatter = timeFormatter;
        this.dateParser = dateParser;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activity = context.getActivity();

        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var dateRange = dateParser.parseDateRange(update);
        if (dateRange.isEmpty()) {
            sendIncorrectFormatMessage(chatId, context);
            return;
        }

        userStateService.setState(user, State.None);
        context.setState(State.None);

        var totalTime = getTotalTime(context, activity, dateRange.get());

        sendMessage(update, context, chatId, activity, totalTime);
    }

    private void sendMessage(
            Update update,
            UpdateContext context,
            Long chatId,
            ActivityDto activity,
            ActivityTotalTimeDto totalTime) {
        var input = update.message().text();
        var name = activity.getName();
        var time = timeFormatter.format(Duration.ofSeconds(totalTime.getDurationSec()));
        var text = "За период " + input + " времени потрачено на активность '" + name + "': " + time;
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void sendIncorrectFormatMessage(Long chatId, UpdateContext context) {
        var text = MessagesHelper.createIncorrectDateRangeFormatMessage();
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private ActivityTotalTimeDto getTotalTime(UpdateContext context, ActivityDto activity, DateRange dateRange)
            throws Exception {
        var apiRequest = new GetActivityTotalTimeRequest(activity.getId(), dateRange.getFrom(), dateRange.getTo());
        var botRequest = new BotRequest<>(apiRequest, context.getMeta());
        return jikanApiClient.activities().getTotalTime(botRequest).getValueOrThrow();
    }

    @Override
    protected State requiredState() {
        return State.SelectCustomDateRangeStatistics;
    }
}
