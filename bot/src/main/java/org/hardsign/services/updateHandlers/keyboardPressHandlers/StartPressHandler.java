package org.hardsign.services.updateHandlers.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.requests.StartActivityRequest;

import java.util.Objects;

public class StartPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public StartPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        if (!context.getState().isDefault())
            return;

        var message = update.message().text();
        if (!Objects.equals(message, ButtonNames.START_TIMESTAMP.getName()))
            return;

        var chatId = update.message().chat().id();
        var activityId = context.getActivityId();
        if (activityId == 0) {
            var replyMarkup = KeyboardFactory.createMainMenu(context, jikanApiClient);
            bot.execute(new SendMessage(chatId, "Вы не выбрали активность. Можете сделать это через главное меню.")
                                .replyMarkup(replyMarkup));
            return;
        }

        var request = new BotRequest<>(new StartActivityRequest(activityId), context.getMeta());
        jikanApiClient.timestamps().start(request).ensureSuccess();

        bot.execute(new SendMessage(chatId, "Трекинг текущей активности начат!")
                            .replyMarkup(KeyboardFactory.createMainMenu(context, jikanApiClient)));
    }
}
