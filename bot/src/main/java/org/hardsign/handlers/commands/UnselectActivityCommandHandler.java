package org.hardsign.handlers.commands;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.commands.abstracts.BaseActivityCommandsHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.services.activities.ActivitiesService;
import org.hardsign.services.users.UserStateService;
import org.jetbrains.annotations.Nullable;

public class UnselectActivityCommandHandler extends BaseActivityCommandsHandler implements CommandHandler {
    private static final String commandPrefix = "/usa_";
    private final TelegramBot bot;
    private final UserStateService userStateService;

    public UnselectActivityCommandHandler(TelegramBot bot, ActivitiesService activitiesService, UserStateService userStateService) {
        super(activitiesService);
        this.bot = bot;
        this.userStateService = userStateService;
    }

    public static String create(long activityId) {
        return commandPrefix + activityId;
    }

    @Override
    protected void handleInternal(User user, @Nullable ActivityDto activity, Update update, UpdateContext context)
            throws Exception {
        var chatId = update.message().chat().id();

        if (context.getActivity() == null || activity == null || activity.getId() != context.getActivity().getId()) {
            handleNoActivity(context, chatId);
            return;
        }

        userStateService.setActivity(user, 0);
        context.setActivity(null);
        context.setActiveTimestamp(null);

        var text = "Вы убрали текущую активность. Лентяйкин! :)";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected String getPrefix() {
        return commandPrefix;
    }

    private void handleNoActivity(UpdateContext context, Long chatId) {
        var text = "Вы еще ничем не занимались. Лентяйкин! :)";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }
}
