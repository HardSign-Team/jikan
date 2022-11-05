package org.hardsign.services.updateHandlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.requests.CreateActivityRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.UserState;
import org.hardsign.services.users.UserStateService;

public class CreateActivityInputHandler implements InputHandler {
    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public CreateActivityInputHandler(TelegramBot bot, JikanApiClient jikanApiClient, UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }
    @Override
    public void handle(Update update, UpdateContext context) throws Exception {
        var user = update.message().from();
        if (user.isBot())
            return;

        if (!context.isRegistered())
            return;

        if (context.getState() != UserState.CreateActivityName)
            return;

        var name = update.message().text();
        var request = new BotRequest<>(new CreateActivityRequest(name), context.getMeta());
        var activity = jikanApiClient.activities().create(request).getValueOrThrow();

        userStateService.setState(user, UserState.None);
        context.setState(UserState.None);

        var chatId = update.message().chat().id();
        var replyMarkup = KeyboardFactory.createMainMenu(context, jikanApiClient);
        bot.execute(new SendMessage(chatId, "Вы создали активность: " + activity.getName())
                            .replyMarkup(replyMarkup));
    }
}
