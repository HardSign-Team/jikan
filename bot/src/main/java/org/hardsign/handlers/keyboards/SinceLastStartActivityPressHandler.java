package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.utils.TimeFormatter;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class SinceLastStartActivityPressHandler extends BaseUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final TimeFormatter timeFormatter;

    public SinceLastStartActivityPressHandler(TelegramBot bot, TimeFormatter timeFormatter) {
        this.bot = bot;
        this.timeFormatter = timeFormatter;
    }

    @Override
    protected boolean checkText(@Nullable String messageText) {
        if (messageText == null) {
            return false;
        }
        return Objects.equals(ButtonNames.LAST_START_STATISTICS.getName(), messageText)
                || Objects.equals(ButtonNames.TIME_SINCE_LAST_START.getName(), messageText);
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var timestamp = context.getActiveTimestamp();
        if (timestamp == null) {
            handleActivityNotStarted(context, activity, chatId);
            return;
        }

        var from = timestamp.getStart().toInstant();
        var to = Instant.ofEpochSecond(update.message().date());

        var totalTime = Duration.between(from, to);

        sendMessage(context, activity, chatId, totalTime);
    }

    private void handleActivityNotStarted(UpdateContext context, ActivityDto activity, Long chatId) {
         var text = "Вы не начинали активность '" + activity.getName() + "'";
         var keyboard = KeyboardFactory.createMainMenu(context);
         bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void sendMessage(UpdateContext context, ActivityDto activity, Long chatId, Duration duration) {
        var keyboard = KeyboardFactory.createMainMenu(context);
        var activityName = activity.getName();
        var durationText = timeFormatter.format(duration);
        var text = "С последнего старта активности '" + activityName + "' прошло: " + durationText;
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
