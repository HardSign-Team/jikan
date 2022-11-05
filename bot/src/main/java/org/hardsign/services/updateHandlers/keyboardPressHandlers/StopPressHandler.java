package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.requests.StopActivityRequest;

import java.time.Duration;
import java.util.Objects;

public class StopPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public StopPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        if (!context.getState().isDefault())
            return;

        var message = update.message().text();
        if (!Objects.equals(message, ButtonNames.STOP_TIMESTAMP.getName()))
            return;

        var chatId = update.message().chat().id();
        if (context.getActivityId() == 0) {
            var replyMarkup = KeyboardFactory.createMainMenu(context, jikanApiClient);
            bot.execute(new SendMessage(chatId, "Вы не выбрали активность. Можете сделать это через главное меню.")
                                .replyMarkup(replyMarkup));
            return;
        }

        var stopRequest = new BotRequest<>(new StopActivityRequest(context.getActivityId()), context.getMeta());
        var timestamp = jikanApiClient.timestamps().stop(stopRequest).getValueOrThrow();

        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(context.getActivityId()), context.getMeta());
        var activity = jikanApiClient.activities().getById(activityRequest).getValueOrThrow();

        assert timestamp.getEnd() != null;
        var duration = Duration.ofMillis(timestamp.getEnd().getTime() - timestamp.getStart().getTime());
        var durationText = getDurationText(duration);
        if (durationText.isBlank())
            durationText = "совсем немножко";
        var text = "Вы остановили трекинг. Времени потрачено на " + activity.getName() + ": " + durationText;
        bot.execute(new SendMessage(chatId, text)
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }

    private String getDurationText(Duration duration) {
        var hours = duration.toHoursPart();
        var minutes = duration.toMinutesPart();
        var sb = new StringBuilder();

        if (hours > 0)
            sb.append(hours).append("ч. ");
        if (minutes > 0)
            sb.append(minutes).append("мин.");

        return sb.toString().trim();
    }
}
