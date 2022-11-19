package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.ValidationHelper;

import java.util.regex.Pattern;

public class DeleteActivityCommandHandler extends BaseUpdateHandler implements CommandHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final Pattern commandPattern = Pattern.compile("/dela_(\\d+)");

    public DeleteActivityCommandHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var text = update.message().text();
        if (text == null)
            return;

        var matcher = commandPattern.matcher(text);
        if (!matcher.matches()) {
            return;
        }

        var activityId = Long.parseLong(matcher.group(1));
        var chatId = update.message().chat().id();
        var currentActivity = context.getActivity();
        if (currentActivity != null && currentActivity.getId() == activityId) {
            handleCurrentActivityError(chatId, context);
            return;
        }

        var activity = getActivity(activityId, context.getMeta());
        if (!ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            handleNotOwnActivityError(chatId, context);
            return;
        }

        handleSuccess(user, context, chatId, activity);
    }

    private ActivityDto getActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        return jikanApiClient.activities().getById(request).getValueOrThrow();
    }

    private void handleSuccess(User user, UpdateContext context, Long chatId, ActivityDto activity) {
        userStateService.update(user, createPatch(activity.getId()));
        context.setState(State.DeleteActivityConfirmation);

        var replyMarkup = new ReplyKeyboardMarkup(
                ButtonNames.ACCEPT_DELETE.getName(),
                ButtonNames.CANCEL_DELETE.getName())
                .resizeKeyboard(true);
        var text = "Вы уверены, что хотите удалить активность " + TelegramUtils.bold(activity.getName()) + "?";
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleNotOwnActivityError(Long chatId, UpdateContext context) {
        var notOwnText = "Ой! А это не ваша активность, вы ее удалить не можете " + Emoji.FaceWithTongue.value();
        bot.execute(new SendMessage(chatId, notOwnText).replyMarkup(KeyboardFactory.createMainMenu(context)));
    }

    private void handleCurrentActivityError(Long chatId, UpdateContext context) {
        var text = "Ай-яй-яй! Нельзя удалить текущую активность.";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private static UserStatePatch createPatch(long activityId) {
        return UserStatePatch.builder()
                .state(State.DeleteActivityConfirmation)
                .deleteActivityId(activityId)
                .build();
    }
}
