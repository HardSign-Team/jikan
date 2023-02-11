package org.hardsign.handlers.commands.abstracts;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.services.ActivitiesService;
import org.jetbrains.annotations.Nullable;

public abstract class BaseActivityCommandsHandler extends BaseIdCommandsHandler {

    private final ActivitiesService activitiesService;

    public BaseActivityCommandsHandler(ActivitiesService activitiesService) {
        this.activitiesService = activitiesService;
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
        handleInternal(user, activitiesService.findActivity(id, context.getMeta()).orElse(null), update, context);
    }
}
