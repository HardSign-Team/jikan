package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.commands.abstracts.BaseActivityCommandsHandler;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.users.State;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.ActivitiesService;
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
            ActivitiesService activitiesService,
            UserStateService userStateService) {
        super(activitiesService);
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
        userStateService.with(context).update(user, createPatch(activity.getId()));

        var replyMarkup = KeyboardFactory.createConfirmationMenu();
        var text = "Вы уверены, что хотите удалить активность " + TelegramUtils.bold(activity.getName()) + "?";
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleActivityNotFoundError(Long chatId, UpdateContext context) {
        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = "Активность не найдена :(";
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleNotOwnActivityError(Long chatId, UpdateContext context) {
        var text = "Ой! А это не ваша активность, вы ее удалить не можете " + Emoji.FaceWithTongue.value();
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void handleCurrentActivityError(Long chatId, UpdateContext context) {
        var text = "Ай-яй-яй! Нельзя удалить текущую активность.";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private static UserStatePatch createPatch(long activityId) {
        return UserStatePatch.builder()
                .state(State.DeleteActivityConfirmation)
                .deleteActivityId(activityId)
                .build();
    }
}
