package org.hardsign.factories;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import org.hardsign.models.ButtonNames;
import org.hardsign.models.UpdateContext;

public class KeyboardFactory {
    public static Keyboard createMainMenu(UpdateContext context) {
        if (!context.isRegistered())
            return new ReplyKeyboardRemove();

        if (!context.getState().isDefault())
            return new ReplyKeyboardRemove();

        var activity = context.getActivity();
        if (activity == null)
            return new ReplyKeyboardMarkup(ButtonNames.ACTIVITIES.getName())
                    .resizeKeyboard(true)
                    .oneTimeKeyboard(true);

        var activeTimestamp = context.getActiveTimestamp();
        if (activeTimestamp == null || activeTimestamp.getEnd() != null)
            return new ReplyKeyboardMarkup(ButtonNames.START_TIMESTAMP.getName(), ButtonNames.ACTIVITIES.getName())
                    .oneTimeKeyboard(true)
                    .resizeKeyboard(true);

        return new ReplyKeyboardMarkup(ButtonNames.STOP_TIMESTAMP.getName(), ButtonNames.ACTIVITIES.getName())
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);
    }
}
