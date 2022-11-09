package org.hardsign.handlers.keyboards;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.DeleteActivityRequest;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.State;
import org.hardsign.handlers.keyboards.abstracts.ConfirmationDeleteActivityPressHandler;
import org.hardsign.services.users.UserStateService;

public class AcceptDeleteActivityPressHandler extends ConfirmationDeleteActivityPressHandler implements KeyboardPressHandler {

    private final TelegramBot bot;
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public AcceptDeleteActivityPressHandler(
            TelegramBot bot,
            JikanApiClient jikanApiClient,
            UserStateService userStateService) {
        this.bot = bot;
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    @Override
    protected String expectedText() {
        return ButtonNames.ACCEPT_DELETE.getName();
    }

    @Override
    protected State requiredState() {
        return State.DeleteActivityConfirmation;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var state = userStateService.getState(user);
        var activityId = state.getDeleteActivityId();

        clearState(user, userStateService, context);

        var chatId = update.message().chat().id();
        if (activityId == 0) {
            handleNotFoundActivity(bot, jikanApiClient, chatId, context);
            return;
        }

        var activity = getActivity(context, activityId);
        if (activity == null) {
            handleNotFoundActivity(bot, jikanApiClient, chatId, context);
            return;
        }

        deleteActivity(activity, context);

        var keyboard = KeyboardFactory.createMainMenu(context, jikanApiClient);
        var text = "Вы удалили активность " + activity.getName() + " " + Emoji.Pensive.value();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard));
    }

    private void deleteActivity(ActivityDto activity, UpdateContext context) throws Exception {
        var request = new BotRequest<>(new DeleteActivityRequest(activity.getId()), context.getMeta());
        jikanApiClient.activities().delete(request).ensureSuccess();
    }

    private ActivityDto getActivity(UpdateContext context, long activityId) throws Exception {
        var request = new BotRequest<>(new GetActivityByIdRequest(activityId), context.getMeta());
        var response = jikanApiClient.activities().getById(request);
        return response.notFound() ? null : response.getValueOrThrow();
    }
}
