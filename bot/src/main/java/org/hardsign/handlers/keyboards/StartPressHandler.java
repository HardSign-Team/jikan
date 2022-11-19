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
import org.hardsign.models.timestamps.TimestampDto;
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
        var activity = context.getActivity();

        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        if (context.getActiveTimestamp() != null) {
            handleAlreadyStartTracking(bot, context, chatId);
            return;
        }

        var timestamp = startTrackActivity(activity.getId(), context.getMeta());
        context.setActiveTimestamp(timestamp);

        var text = "Трекинг активности " + activity.getName() + " начат!";
        var keyboard = KeyboardFactory.createMainMenu(context);
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void handleAlreadyStartTracking(TelegramBot bot, UpdateContext context, Long chatId) {
        var text = "Вы уже трекаете текущую активность.";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected String expectedText() {
        return ButtonNames.START_TIMESTAMP.getName();
    }

    private TimestampDto startTrackActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new StartActivityRequest(activityId), meta);
        return jikanApiClient.timestamps().start(request).getValueOrThrow();
    }
}
