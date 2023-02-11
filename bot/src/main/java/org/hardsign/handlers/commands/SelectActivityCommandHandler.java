package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.commands.abstracts.BaseActivityCommandsHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.services.ActivitiesService;
import org.hardsign.services.TimestampsService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.ValidationHelper;
import org.jetbrains.annotations.Nullable;

import static org.hardsign.utils.TelegramUtils.bold;

public class SelectActivityCommandHandler extends BaseActivityCommandsHandler implements CommandHandler {

    private static final String commandPrefix = "/sa_";
    private final TelegramBot bot;
    private final TimestampsService timestampsService;
    private final UserStateService userStateService;

    public SelectActivityCommandHandler(TelegramBot bot, ActivitiesService activitiesService, TimestampsService timestampsService, UserStateService userStateService) {
        super(activitiesService);
        this.bot = bot;
        this.timestampsService = timestampsService;
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
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        if (!ValidationHelper.isOwnActivity(context.getUser(), activity)) {
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        userStateService.setActivity(user, activity.getId());
        var activeTimestamp = timestampsService.findActiveTimestamp(activity.getId(), context.getMeta()).orElse(null);
        context.setActivity(activity);
        context.setActiveTimestamp(activeTimestamp);

        var text = "Вы выбрали активность: " + bold(activity.getName());
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    @Override
    protected String getPrefix() {
        return commandPrefix;
    }
}
