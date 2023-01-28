package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.StopActivityRequest;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.TimeFormatter;

import java.time.Duration;

public class StopPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final TimeFormatter timeFormatter;

    public StopPressHandler(TelegramBot bot, JikanApiClient jikanApiClient, TimeFormatter timeFormatter) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.timeFormatter = timeFormatter;
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
        var duration = Duration.ofMillis(timestamp.getEnd().toEpochMilli() - timestamp.getStart().toEpochMilli());
        var durationText = timeFormatter.format(duration);

        var activityName = TelegramUtils.bold(activity.getName());
        var text = "Вы остановили трекинг. Времени потрачено на " + activityName + ": " + durationText;
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleNotStartedActivity(TelegramBot bot, UpdateContext context, Long chatId) {
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

}
