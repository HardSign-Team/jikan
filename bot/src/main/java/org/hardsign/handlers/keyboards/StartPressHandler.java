package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.services.activities.ActivitiesService;
import org.hardsign.utils.TelegramUtils;

public class StartPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final ActivitiesService activitiesService;

    public StartPressHandler(TelegramBot bot, ActivitiesService activitiesService) {
        this.bot = bot;
        this.activitiesService = activitiesService;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        if (context.hasActiveTimestamp()) {
            handleAlreadyStartTracking(bot, context, chatId);
            return;
        }

        var timestamp = activitiesService.startTracking(activity.getId(), context.getMeta());
        context.setActiveTimestamp(timestamp);

        var text = "Трекинг активности " + TelegramUtils.bold(activity.getName()) + " начат!";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void handleAlreadyStartTracking(TelegramBot bot, UpdateContext context, Long chatId) {
        var text = "Вы уже трекаете текущую активность.";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected String expectedText() {
        return ButtonNames.START_TIMESTAMP.getName();
    }
}
