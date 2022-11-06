package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetAllActivitiesRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.services.updateHandlers.BaseTextUpdateHandler;

public class DeleteActivityPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public DeleteActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var activities = getActivities(context.getMeta());
        var chatId = update.message().chat().id();
        var replyMarkup = new ReplyKeyboardMarkup(ButtonNames.BACK.getName())
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        bot.execute(new SendMessage(chatId, toText(activities)).replyMarkup(replyMarkup));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.DELETE_ACTIVITY.getName();
    }

    private String toText(ActivityDto[] activities) {
        if (activities.length == 0)
            return "У тебя еще нет активностей. Можешь добавить их :)";

        var sb = new StringBuilder();
        for (var i = 0; i < activities.length; i++) {
            sb.append(i + 1)
                    .append(". ").append(activities[i].getName()).append(". Удалить: /dela_")
                    .append(activities[i].getId()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private ActivityDto[] getActivities(TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetAllActivitiesRequest(), meta);
        return jikanApiClient.activities().getAll(request).getValueOrThrow();
    }
}
