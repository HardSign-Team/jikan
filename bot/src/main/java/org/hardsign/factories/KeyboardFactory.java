package org.hardsign.factories;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.HttpCodes;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.requests.BotRequest;
import org.hardsign.models.timestamps.requests.GetLastTimestampByActivityIdRequest;

public class KeyboardFactory {
    public static Keyboard createMainMenu(UpdateContext context, JikanApiClient jikanApiClient) throws Exception {
        var activityId = context.getActivityId();

        if (!context.isRegistered())
            return new ReplyKeyboardRemove();

        if (!context.getState().isDefault())
            return new ReplyKeyboardRemove();

        if (activityId == 0)
            return new ReplyKeyboardMarkup(ButtonNames.ACTIVITIES.getName())
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(true);

        var meta = context.getMeta();
        var request = new BotRequest<>(new GetLastTimestampByActivityIdRequest(activityId), meta);
        var timestampResponse = jikanApiClient.timestamps().getLast(request);
        if (HttpCodes.NotFound.is(timestampResponse.getCode()) || timestampResponse.getValueOrThrow().getEnd() != null)
            return new ReplyKeyboardMarkup(ButtonNames.START_TIMESTAMP.getName(), ButtonNames.ACTIVITIES.getName())
                    .resizeKeyboard(true);

        return new ReplyKeyboardMarkup(ButtonNames.STOP_TIMESTAMP.getName(), ButtonNames.ACTIVITIES.getName())
                .resizeKeyboard(true);
    }
}
