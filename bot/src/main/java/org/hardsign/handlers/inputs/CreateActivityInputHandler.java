package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.services.activities.ActivitiesService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.TelegramUtils;

public class CreateActivityInputHandler extends BaseUpdateHandler implements InputHandler {
    private final TelegramBot bot;
    private final ActivitiesService activitiesService;
    private final UserStateService userStateService;

    public CreateActivityInputHandler(TelegramBot bot, ActivitiesService activitiesService, UserStateService userStateService) {
        this.bot = bot;
        this.activitiesService = activitiesService;
        this.userStateService = userStateService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var name = update.message().text();
        var activity = activitiesService.create(name, context.getMeta());

        userStateService.with(context).setState(user, State.None);

        var chatId = update.message().chat().id();
        var replyMarkup = KeyboardFactory.createMainMenu(context);
        var text = "Вы создали активность: " + TelegramUtils.bold(activity.getName());
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup).parseMode(TelegramUtils.PARSE_MODE));
    }

    @Override
    protected State requiredState() {
        return State.CreateActivityName;
    }
}
