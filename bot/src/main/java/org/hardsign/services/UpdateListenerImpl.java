package org.hardsign.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.TelegramUserMetaFactory;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.services.updateHandlers.UpdateHandler;
import org.hardsign.services.updateHandlers.commands.CreateActivityCommandHandler;
import org.hardsign.services.updateHandlers.commands.StartCommandHandler;
import org.hardsign.services.updateHandlers.inputs.CreateActivityInputHandler;
import org.hardsign.services.updateHandlers.keyboardPressHandlers.*;
import org.hardsign.services.users.UserStateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;
    private final List<UpdateHandler> updateHandlers = new ArrayList<>();

    public UpdateListenerImpl(
            JikanApiClient jikanApiClient,
            TelegramBot bot,
            UserStateService userStateService) {
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;

        updateHandlers.add(new StartCommandHandler(bot, jikanApiClient));
        updateHandlers.add(new CreateActivityCommandHandler(bot, userStateService));

        updateHandlers.add(new CreateActivityInputHandler(bot, jikanApiClient, userStateService));

        updateHandlers.add(new ActivitiesPressHandler(bot, jikanApiClient));
        updateHandlers.add(new SelectActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new UnSelectActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new BackPressHandler(bot, jikanApiClient));
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
        var context = UpdateContext.builder()
                .isRegistered(isUserRegistered(user, meta))
                .meta(meta)
                .state(state.getState())
                .activityId(state.getActivityId())
                .build();

        for (var handler : updateHandlers) {
            try {
                handler.handle(update, context);
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
            }
        }

        return update;
    }

    private boolean isUserRegistered(User user, TelegramUserMeta meta) {
        if (user.isBot())
            return true;
        var findUserByLoginRequest = new FindUserByLoginRequest(Long.toString(meta.getId()));
        var apiUser = jikanApiClient.users().findByLogin(new BotRequest<>(findUserByLoginRequest, meta));
        return apiUser.isSuccess() && apiUser.getCode() == 200; // todo: (tebaikin) 05.11.2022 refactor
    }
}