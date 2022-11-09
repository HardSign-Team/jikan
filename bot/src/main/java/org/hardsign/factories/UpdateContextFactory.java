package org.hardsign.factories;

import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.JikanResponse;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.GetLastTimestampByActivityIdRequest;
import org.hardsign.models.users.UserDto;
import org.hardsign.models.users.requests.FindUserByLoginRequest;
import org.hardsign.services.users.UserStateService;

import java.util.Optional;
import java.util.logging.Logger;

public class UpdateContextFactory {
    private static final Logger LOGGER = Logger.getLogger(UpdateContextFactory.class.getName());
    private final JikanApiClient jikanApiClient;
    private final UserStateService userStateService;

    public UpdateContextFactory(JikanApiClient jikanApiClient, UserStateService userStateService) {
        this.jikanApiClient = jikanApiClient;
        this.userStateService = userStateService;
    }

    public UpdateContext create(User user) {
        var meta = TelegramUserMetaFactory.create(user);
        var state = userStateService.getState(user);
        var apiUser = findUser(user, meta);
        var activity = findActivity(state.getActivityId(), meta);
        var activeTimestamp = findTimestamp(state.getActivityId(), meta);
        return UpdateContext.builder()
                .isRegistered(apiUser.isPresent())
                .user(apiUser.orElse(null))
                .meta(meta)
                .state(state.getState())
                .activity(activity.orElse(null))
                .activeTimestamp(activeTimestamp.orElse(null))
                .build();
    }

    private Optional<UserDto> findUser(User user, TelegramUserMeta meta) {
        if (user.isBot())
            return Optional.empty();
        var request = new FindUserByLoginRequest(Long.toString(meta.getId()));
        var apiUser = jikanApiClient.users().findByLogin(request);
        return getValueSafety(apiUser);
    }

    private Optional<ActivityDto> findActivity(long activityId, TelegramUserMeta meta) {
        if (activityId == 0)
            return Optional.empty();
        var request = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        var activityResponse = jikanApiClient.activities().getById(request);
        return getValueSafety(activityResponse);
    }

    private Optional<TimestampDto> findTimestamp(long activityId, TelegramUserMeta meta) {
        if (activityId == 0)
            return Optional.empty();
        var request = new BotRequest<>(new GetLastTimestampByActivityIdRequest(activityId), meta);
        var timestampResponse = jikanApiClient.timestamps().getLast(request);
        return getValueSafety(timestampResponse).filter(x -> x.getEnd() == null);
    }

    private <T> Optional<T> getValueSafety(JikanResponse<T> response) {
        if (response.notFound())
            return Optional.empty();
        try {
            return Optional.of(response.getValueOrThrow());
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            return Optional.empty();
        }
    }
}
