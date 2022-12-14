package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.commands.abstracts.BaseActivityCommandsHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.ValidationHelper;
import org.jetbrains.annotations.Nullable;

public class DeleteActivityCommandHandler extends BaseActivityCommandsHandler implements CommandHandler {

    private static final String commandPrefix = "/dela_";
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public DeleteActivityCommandHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
        super(jikanApiClient);
        this.bot = bot;
        this.userStateService = userStateService;
    }

    public static String create(long activityId) {
        return commandPrefix + activityId;
    }

    @Override
    protected void handleInternal(
            User user,
            @Nullable ActivityDto activity,
            Update update,
            UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        if (activity == null) {
            handleActivityNotFoundError(chatId, context);
            return;
        }

        var currentActivity = context.getActivity();
        if (currentActivity != null && currentActivity.getId() == activity.getId()) {
            handleCurrentActivityError(chatId, context);
            return;
        }

        if (!ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            handleNotOwnActivityError(chatId, context);
            return;
        }

        handleSuccess(user, context, chatId, activity);
    }

    @Override
    protected String getPrefix() {
        return commandPrefix;
    }

    private void handleSuccess(User user, UpdateContext context, Long chatId, ActivityDto activity) {
        userStateService.update(user, createPatch(activity.getId()));
        context.setState(State.DeleteActivityConfirmation);

        var replyMarkup = new ReplyKeyboardMarkup(
                ButtonNames.ACCEPT_DELETE.getName(),
                ButtonNames.CANCEL_DELETE.getName())
                .resizeKeyboard(true);
        var text = "???? ??????????????, ?????? ???????????? ?????????????? ???????????????????? " + TelegramUtils.bold(activity.getName()) + "?";
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleActivityNotFoundError(Long chatId, UpdateContext context) {
        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = "???????????????????? ???? ?????????????? :(";
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleNotOwnActivityError(Long chatId, UpdateContext context) {
        var notOwnText = "????! ?? ?????? ???? ???????? ????????????????????, ???? ???? ?????????????? ???? ???????????? " + Emoji.FaceWithTongue.value();
        bot.execute(new SendMessage(chatId, notOwnText).replyMarkup(KeyboardFactory.createMainMenu(context)));
    }

    private void handleCurrentActivityError(Long chatId, UpdateContext context) {
        var text = "????-????-????! ???????????? ?????????????? ?????????????? ????????????????????.";
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
