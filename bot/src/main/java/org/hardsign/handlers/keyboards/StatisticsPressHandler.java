package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.handlers.BaseTextUpdateHandler;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;

public class StatisticsPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {
    private final TelegramBot bot;

    public StatisticsPressHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.STATISTICS.getName();
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        if (!context.hasSelectedActivity()) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var keyboard = KeyboardFactory.createStatisticsMenu(context);
        bot.execute(new SendMessage(chatId, "Выберите статистику").replyMarkup(keyboard));
    }
}
