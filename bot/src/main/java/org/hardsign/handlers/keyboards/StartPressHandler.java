package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.requests.StartActivityRequest;
import org.hardsign.handlers.BaseTextUpdateHandler;

public class StartPressHandler extends BaseTextUpdateHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;

    public StartPressHandler(TelegramBot bot, JikanApiClient jikanApiClient) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activityId = context.getActivityId();
        if (activityId == 0) {
            handleNoCurrentActivity(bot, jikanApiClient, context, chatId);
            return;
        }

        startTrackActivity(activityId, context.getMeta());

        var text = "Трекинг текущей активности начат!";
        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    @Override
    protected String expectedText() {
        return ButtonNames.START_TIMESTAMP.getName();
    }

    private void startTrackActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new StartActivityRequest(activityId), meta);
        jikanApiClient.timestamps().start(request).ensureSuccess();
    }
}
