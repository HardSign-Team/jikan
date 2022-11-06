package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.StopActivityRequest;
import org.hardsign.services.updateHandlers.BaseTextUpdateHandler;

import java.time.Duration;

public class StopPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public StopPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        if (context.getActivityId() == 0) {
            handleNoCurrentActivity(bot, jikanApiClient, context, chatId);
            return;
        }

        var timestamp = stopTracking(context.getActivityId(), context.getMeta());
        var activity = getActivity(timestamp.getActivityId(), context.getMeta());

        assert timestamp.getEnd() != null;
        var duration = Duration.ofMillis(timestamp.getEnd().getTime() - timestamp.getStart().getTime());
        var durationText = getDurationText(duration);

        var text = "Вы остановили трекинг. Времени потрачено на " + activity.getName() + ": " + durationText;
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private ActivityDto getActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        return jikanApiClient.activities().getById(activityRequest).getValueOrThrow();
    }

    private TimestampDto stopTracking(long activityId, TelegramUserMeta meta) throws Exception {
        var stopRequest = new BotRequest<>(new StopActivityRequest(activityId), meta);
        return jikanApiClient.timestamps().stop(stopRequest).getValueOrThrow();
    }

    @Override
    protected String expectedText() {
        return ButtonNames.STOP_TIMESTAMP.getName();
    }

    private String getDurationText(Duration duration) {
        var hours = duration.toHoursPart();
        var minutes = duration.toMinutesPart();
        var sb = new StringBuilder();

        if (hours > 0)
            sb.append(hours).append("ч. ");
        if (minutes > 0)
            sb.append(minutes).append("мин.");

        var result =  sb.toString().trim();

        if (result.isBlank())
            return "совсем немножко";
        return result;
    }
}
