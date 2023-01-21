package org.hardsign.handlers.keyboards.abstracts;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.handlers.keyboards.KeyboardPressHandler;
import org.hardsign.models.DateRange;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityTotalTimeDto;
import org.hardsign.models.activities.requests.GetActivityTotalTimeRequest;
import org.hardsign.models.requests.BotRequest;

import java.time.Duration;

public abstract class PeriodActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public PeriodActivityPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    protected abstract DateRange getDateRange(Update update);

    protected abstract String getTextMessage(ActivityDto activity, Duration duration);

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

    private void sendMessage(UpdateContext context, ActivityDto activity, Long chatId, ActivityTotalTimeDto totalTime) {
        var duration = Duration.ofSeconds(totalTime.getDurationSec());
        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = getTextMessage(activity, duration);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
