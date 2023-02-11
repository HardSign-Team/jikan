package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.handlers.keyboards.abstracts.ConfirmationDeleteActivityPressHandler;
import org.hardsign.models.users.UserStatePatch;
import org.hardsign.services.ActivitiesService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;

public class AcceptDeleteActivityPressHandler extends ConfirmationDeleteActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final ActivitiesService activitiesService;
    private final UserStateService userStateService;

    public AcceptDeleteActivityPressHandler(
            TelegramBot bot,
            ActivitiesService activitiesService,
            UserStateService userStateService) {
        this.bot = bot;
        this.activitiesService = activitiesService;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACCEPT_DELETE.getName();
    }

    @Override
    protected State requiredState() {
        return State.DeleteActivityConfirmation;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var state = userStateService.getState(user);
        var activityId = state.getDeleteActivityId();

        userStateService.with(context).update(user, UserStatePatch.createDefault());

        var chatId = update.message().chat().id();
        if (activityId == 0) {
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        var activity = activitiesService.findActivity(activityId, context.getMeta()).orElse(null);
        if (activity == null) {
            handleNotFoundActivity(bot, chatId, context);
            return;
        }

        activitiesService.deleteActivity(activity.getId(), context.getMeta());

        var keyboard = KeyboardFactory.createMainMenu(context);
        var text = "Вы удалили активность " + TelegramUtils.bold(activity.getName()) + " " + Emoji.Pensive.value();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }
}
