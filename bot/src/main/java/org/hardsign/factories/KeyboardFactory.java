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
                    .resizeKeyboard(true);

        var activeTimestamp = context.getActiveTimestamp();
        if (activeTimestamp == null || activeTimestamp.getEnd() != null)
            return new ReplyKeyboardMarkup(
                    new String[]{

                            ButtonNames.START_TIMESTAMP.getName(),
                            ButtonNames.ACTIVITIES.getName(),
                    },
                    new String[]{
                            ButtonNames.ACTIVITY_MENU.getName()
                    })
                    .resizeKeyboard(true);

        return new ReplyKeyboardMarkup(
                new String[]{
                        ButtonNames.STOP_TIMESTAMP.getName(),
                        ButtonNames.ACTIVITIES.getName(),
                },
                new String[]{
                        ButtonNames.TIME_SINCE_LAST_START.getName(),
                        ButtonNames.ACTIVITY_MENU.getName()
                })
                .resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createActivitiesMenu() {
        return new ReplyKeyboardMarkup(
                ButtonNames.CREATE_ACTIVITY.getName(),
                ButtonNames.BACK.getName()
        ).resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createStatisticsMenu(UpdateContext context) {
        if (context.getActiveTimestamp() != null)
            return new ReplyKeyboardMarkup(
                new String[]{
                        ButtonNames.CURRENT_DAY_STATISTICS.getName(), ButtonNames.CURRENT_MONTH_STATISTICS.getName()
                },
                new String[]{
                        ButtonNames.CUSTOM_DATE_STATISTICS.getName(), ButtonNames.LAST_START_STATISTICS.getName()
                },
                new String[]{
                        ButtonNames.BACK.getName()
                })
                .resizeKeyboard(true);

        return new ReplyKeyboardMarkup(
                new String[]{
                        ButtonNames.CURRENT_DAY_STATISTICS.getName(), ButtonNames.CURRENT_MONTH_STATISTICS.getName()
                },
                new String[]{
                        ButtonNames.CUSTOM_DATE_STATISTICS.getName(), ButtonNames.BACK.getName()
                })
                .resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createBackButtonMenu() {
        return new ReplyKeyboardMarkup(ButtonNames.BACK.getName())
                .resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createActivityMenu() {
        return new ReplyKeyboardMarkup(
                new String[] {
                        ButtonNames.TIMESTAMPS.getName(), ButtonNames.STATISTICS.getName()
                },
                new String[] {
                        ButtonNames.BACK.getName()
                }
        )
                .resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createTimestampsMenu() {
        return new ReplyKeyboardMarkup(
                new String[]{
                        ButtonNames.ADD_TIMESTAMP.getName(), ButtonNames.CUSTOM_DATE_TIMESTAMPS.getName()
                },
                new String[]{
                        ButtonNames.BACK.getName()
                }
        )
                .resizeKeyboard(true);
    }

    public static ReplyKeyboardMarkup createConfirmationMenu() {
        return new ReplyKeyboardMarkup(
                ButtonNames.ACCEPT_DELETE.getName(),
                ButtonNames.CANCEL_DELETE.getName())
                .resizeKeyboard(true);
    }
}
