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
import org.hardsign.services.updateHandlers.commands.SelectActivityCommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActivitiesPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public ActivitiesPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var activities = getActivities(context.getMeta());
        var text = toText(activities);
        var replyMarkup = new ReplyKeyboardMarkup(getButtons(context))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        bot.execute(new SendMessage(update.message().chat().id(), text).replyMarkup(replyMarkup));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACTIVITIES.getName();
    }

    @NotNull
    private static String[] getButtons(UpdateContext context) {
        var buttons = new ArrayList<String>();
        buttons.add(ButtonNames.CREATE_ACTIVITY.getName());
        buttons.add(ButtonNames.DELETE_ACTIVITY.getName());
        if (context.getActivityId() != 0)
            buttons.add(ButtonNames.UNSELECT_ACTIVITY.getName());
        buttons.add(ButtonNames.BACK.getName());
        return buttons.toArray(new String[0]);
    }

    private String toText(ActivityDto[] activities) {
        if (activities.length == 0)
            return "У тебя еще нет активностей. Можешь добавить их :)";

        var command = SelectActivityCommandHandler.commandPrefix;
        var sb = new StringBuilder();
        for (var i = 0; i < activities.length; i++) {
            sb.append(i + 1)
                    .append(". ").append(activities[i].getName()).append(". Выбрать: ").append(command)
                    .append(activities[i].getId()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private ActivityDto[] getActivities(TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetAllActivitiesRequest(), meta);
        return jikanApiClient.activities().getAll(request).getValueOrThrow();
    }
}
