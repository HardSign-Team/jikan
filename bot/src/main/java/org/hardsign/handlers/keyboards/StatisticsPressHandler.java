package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
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
        if (context.getActivity() == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var keyboard = new ReplyKeyboardMarkup(
                ButtonNames.CUSTOM_DATE_STATISTICS.getName(),
                ButtonNames.CURRENT_MONTH_STATISTICS.getName(),
                ButtonNames.BACK.getName())
                .resizeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Выберите статистику").replyMarkup(keyboard));
    }
}
