package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.commands.abstracts.BaseActivityCommandsHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.ValidationHelper;
import org.jetbrains.annotations.Nullable;

public class SelectActivityCommandHandler extends BaseActivityCommandsHandler implements CommandHandler {

    private static final String commandPrefix = "/sa_";
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public SelectActivityCommandHandler(TelegramBot bot, JikanApiClient jikanApiClient, UserStateService userStateService) {
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
            sendMessage(chatId, "Активность не найдена :(", context);
            return;
        }

        if (!ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            sendMessage(chatId, "Активность не найдена :(", context);
            return;
        }

        handleSuccess(user, context, chatId, activity);
    }

    @Override
    protected String getPrefix() {
        return commandPrefix;
    }

    private void handleSuccess(User user, UpdateContext context, Long chatId, ActivityDto activity) throws Exception {
        userStateService.setActivity(user, activity.getId());
        context.setActivity(activity);
        context.setActiveTimestamp(getActiveTimestamp(activity.getId(), context.getMeta()));

        var text = "Вы выбрали активность: " + TelegramUtils.bold(activity.getName());
        sendMessage(chatId, text, context);
    }

    private void sendMessage(Long chatId, String text, UpdateContext context) {
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }
}

