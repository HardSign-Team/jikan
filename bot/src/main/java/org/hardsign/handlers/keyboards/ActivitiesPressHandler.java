package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.ActivitiesListFactory;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.services.activities.ActivitiesService;
import org.hardsign.utils.TelegramUtils;

public class ActivitiesPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;
    private final ActivitiesService activitiesService;
    private final ActivitiesListFactory activitiesListFactory;

    public ActivitiesPressHandler(TelegramBot bot, ActivitiesService activitiesService) {
        this.bot = bot;
        this.activitiesService = activitiesService;
        this.activitiesListFactory = new ActivitiesListFactory();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activities = activitiesService.getActivitiesOverview(context.getMeta());
        var text = activitiesListFactory.create(activities, context);
        var replyMarkup = KeyboardFactory.createActivitiesMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(replyMarkup).parseMode(TelegramUtils.PARSE_MODE));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACTIVITIES.getName();
    }
}
