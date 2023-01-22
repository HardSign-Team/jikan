package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;


public class ActivityMenuPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;

    public ActivityMenuPressHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACTIVITY_MENU.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();

        var activity = context.getActivity();
        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var text = "Вы открыли меню активности '" + activity.getName() + "'. Выберите действие";
        var keyboard = KeyboardFactory.createActivityMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }
}
