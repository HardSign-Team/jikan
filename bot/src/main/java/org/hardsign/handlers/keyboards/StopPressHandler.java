package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.exceptions.BotException;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.StopActivityRequest;
import org.hardsign.handlers.BaseTextUpdateHandler;

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
        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        if (context.getActiveTimestamp() == null) {
            handleNotStartedActivity(bot, context, chatId);
            return;
        }

        var timestamp = stopTracking(activity.getId(), context.getMeta());
        context.setActiveTimestamp(null);

        assert timestamp.getEnd() != null;
        var duration = Duration.ofMillis(timestamp.getEnd().getTime() - timestamp.getStart().getTime());
        var durationText = getDurationText(duration);

        var text = "Вы остановили трекинг. Времени потрачено на " + activity.getName() + ": " + durationText;
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void handleNotStartedActivity(TelegramBot bot, UpdateContext context, Long chatId) throws BotException {
        var text = "Вы не начинали трекать активность.";
        sendDefaultMenuMessage(bot, context, chatId, text);
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
