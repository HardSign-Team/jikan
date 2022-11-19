package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetAllActivitiesRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.handlers.commands.SelectActivityCommandHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Optional;

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
        var text = toText(activities, context);
        var replyMarkup = new ReplyKeyboardMarkup(getButtons(context))
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        bot.execute(new SendMessage(update.message().chat().id(), text).replyMarkup(replyMarkup).parseMode(ParseMode.HTML));
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
        if (context.getActivity() != null)
            buttons.add(ButtonNames.UNSELECT_ACTIVITY.getName());
        buttons.add(ButtonNames.BACK.getName());
        return buttons.toArray(new String[0]);
    }

    private String toText(ActivityDto[] activities, UpdateContext context) {
        if (activities.length == 0)
            return "У тебя еще нет активностей. Можешь добавить их :)";

        long currentActivityId = Optional.ofNullable(context.getActivity())
                .map(ActivityDto::getId)
                .orElse(0L);

        var newLine = System.lineSeparator();
        var sb = new StringBuilder();
        for (var i = 0; i < activities.length; i++) {
            var activity = activities[i];
            sb.append(i + 1).append(". ");
            if (currentActivityId == activity.getId()) {
                appendName(sb, activity).append(' ');
                appendActivitySelected(sb).append(newLine);
            } else {
                appendName(sb, activity).append(newLine);
                appendSelectCommand(sb, activity).append(newLine);
            }
        }
        return sb.toString();
    }


    private StringBuilder appendName(StringBuilder sb, ActivityDto activity) {
        return sb.append(activity.getName()).append('.');
    }

    private StringBuilder appendSelectCommand(StringBuilder sb, ActivityDto activity) {
        var command = SelectActivityCommandHandler.create(activity.getId());
        return sb.append("Выбрать: ").append(command);
    }

    private StringBuilder appendActivitySelected(StringBuilder sb) {
        return sb.append("<b>[Выбрана]</b>");
    }

    private ActivityDto[] getActivities(TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetAllActivitiesRequest(), meta);
        return jikanApiClient.activities().getAll(request).getValueOrThrow();
    }
}
