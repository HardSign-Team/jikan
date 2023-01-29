package org.hardsign.handlers.commands.abstracts;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.requests.GetActivityByIdRequest;
import org.hardsign.models.auth.TelegramUserMeta;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.models.timestamps.requests.GetLastTimestampByActivityIdRequest;
import org.jetbrains.annotations.Nullable;

public abstract class BaseActivityCommandsHandler extends BaseIdCommandsHandler {

    private final JikanApiClient jikanApiClient;

    public BaseActivityCommandsHandler(JikanApiClient jikanApiClient) {
        this.jikanApiClient = jikanApiClient;
    }

    protected abstract void handleInternal(
            User user,
            @Nullable ActivityDto activity,
            Update update,
            UpdateContext context)
            throws Exception;

    protected abstract String getPrefix();

    @Override
    protected void handleInternal(User user, Long id, Update update, UpdateContext context) throws Exception {
        handleInternal(user, getActivity(id, context.getMeta()), update, context);
    }

    @Nullable
    protected TimestampDto getActiveTimestamp(long activityId, TelegramUserMeta meta) throws Exception {
        var request = new BotRequest<>(new GetLastTimestampByActivityIdRequest(activityId), meta);
        var response = jikanApiClient.timestamps().getLast(request);
        if (response.notFound())
            return null;
        response.ensureSuccess();
        return response.getValue().orElse(null);
    }

    @Nullable
    private ActivityDto getActivity(long activityId, TelegramUserMeta meta) throws Exception {
        var activityRequest = new BotRequest<>(new GetActivityByIdRequest(activityId), meta);
        var result = jikanApiClient.activities().getById(activityRequest);
        return result.notFound() ? null : result.getValueOrThrow();
    }
}
