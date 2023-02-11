package org.hardsign.handlers.keyboards.abstracts;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.handlers.keyboards.KeyboardPressHandler;
import org.hardsign.models.DateRange;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.services.ActivitiesService;

import java.time.Duration;

public abstract class PeriodActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final ActivitiesService activitiesService;

    public PeriodActivityPressHandler(TelegramBot bot, ActivitiesService activitiesService) {
        this.bot = bot;
        this.activitiesService = activitiesService;
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
        var totalTime = activitiesService.getTotalTime(activity.getId(), dateRange, context.getMeta());

        var duration = Duration.ofSeconds(totalTime.getDurationSec());
        var text = getTextMessage(activity, duration);
        sendDefaultMenuMessage(bot, context, chatId, text);
    }
}
