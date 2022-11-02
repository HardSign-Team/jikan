package org.hardsign.keyboardPressHandlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.models.activities.ActivityDto;

import java.util.UUID;

public class ActivitiesPressHandler implements KeyboardPressHandler {
    private final TelegramBot bot;

    public ActivitiesPressHandler(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void handle(Update update) {
        var userId = update.message().from().id();
        var activities = getActivities(userId);
        var text = toText(activities);
        var request = new SendMessage(update.message().chat().id(), text)
                .replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton[][]{
                        new KeyboardButton[] {
                                new KeyboardButton(ButtonNames.CREATE_ACTIVITY.getName())
                        }
                }));
        var response = bot.execute(request);
    }

    private String toText(ActivityDto[] activities) {
        var sb = new StringBuilder();
        for (int i = 0; i < activities.length; i++) {
            sb.append(i)
                    .append(". ")
                    .append(activities[i].getName())
                    .append(".")
                    .append(System.lineSeparator())
                    .append("Select: ")
                    .append("/sa_")
                    .append(activities[i].getId()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private ActivityDto[] getActivities(Long userId) {
        // TODO: 30.10.2022 not implemented. Uses mocked values
        return new ActivityDto[] {
          new ActivityDto(UUID.randomUUID(), "Work"),
          new ActivityDto(UUID.randomUUID(), "Study"),
          new ActivityDto(UUID.randomUUID(), "Books"),
          new ActivityDto(UUID.randomUUID(), "Games"),
        };
    }
}
