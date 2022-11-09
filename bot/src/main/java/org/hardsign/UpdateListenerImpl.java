package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.TelegramUserMetaFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.handlers.UpdateHandler;
import org.hardsign.handlers.commands.CreateActivityCommandHandler;
import org.hardsign.handlers.commands.DeleteActivityCommandHandler;
import org.hardsign.handlers.commands.SelectActivityCommandHandler;
import org.hardsign.handlers.commands.StartCommandHandler;
import org.hardsign.handlers.inputs.CreateActivityInputHandler;
import org.hardsign.services.users.UserStateService;
import org.hardsign.handlers.keyboards.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final JikanApiClient jikanApiClient;
    private final TelegramBot bot;
    private final UserStateService userStateService;
    private final List<UpdateHandler> updateHandlers = new ArrayList<>();

    public UpdateListenerImpl(
            JikanApiClient jikanApiClient,
            TelegramBot bot,
            UserStateService userStateService) {
        this.jikanApiClient = jikanApiClient;
        this.bot = bot;
        this.userStateService = userStateService;

        updateHandlers.add(new StartCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new CreateActivityCommandHandler(bot, userStateService));
        updateHandlers.add(new SelectActivityCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new DeleteActivityCommandHandler(bot, jikanApiClient, userStateService));

        updateHandlers.add(new CreateActivityInputHandler(bot, jikanApiClient, userStateService));

        updateHandlers.add(new ActivitiesPressHandler(bot, jikanApiClient));
        updateHandlers.add(new UnselectActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new BackPressHandler(bot, jikanApiClient));
        updateHandlers.add(new AcceptDeleteActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new CancelDeleteActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new DeleteActivityPressHandler(bot, jikanApiClient));
        updateHandlers.add(new StartPressHandler(bot, jikanApiClient));
        updateHandlers.add(new StopPressHandler(bot, jikanApiClient));
    }

    @Override
    public int process(List<Update> list) {
        return list.stream()
                .map(this::process)
                .reduce((x, y) -> y).map(Update::updateId)
                .orElse(CONFIRMED_UPDATES_ALL);
    }

    private Update process(Update update) {
        var content = update.message().text();
        if (Objects.equals(content, ""))
            return update;

        var user = update.message().from();
        var meta = TelegramUserMetaFactory.create(user);
        var state = userStateService.getState(user);
        var apiUser = findUser(user, meta);
        var context = UpdateContext.builder()
                .isRegistered(apiUser.isPresent())
                .user(apiUser.orElse(null))
                .meta(meta)
                .state(state.getState())
                .activityId(state.getActivityId())
                .build();

        var exceptionThrown = false;
        for (var handler : updateHandlers) {
            try {
                handler.handle(update, context);
            } catch (Exception e) {
                exceptionThrown = true;
                LOGGER.severe(e.getMessage());
            }
        }

        if (exceptionThrown) {
            sendErrorMessage(update.message().chat().id());
        }

        return update;
    }

    private void sendErrorMessage(Long chatId) {
        bot.execute(new SendMessage(chatId, "Произошла ошибка :( Попробуй ещё раз!"));
    }

    private Optional<UserDto> findUser(User user, TelegramUserMeta meta) {
        if (user.isBot())
            return Optional.empty();
        var request = new FindUserByLoginRequest(Long.toString(meta.getId()));
        var apiUser = jikanApiClient.users().findByLogin(request);
        return apiUser.isSuccess() && apiUser.isOk()
                ? apiUser.getValue()
                : Optional.empty();
    }
}