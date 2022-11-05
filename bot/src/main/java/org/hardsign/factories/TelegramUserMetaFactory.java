package org.hardsign.factories;

import com.pengrad.telegrambot.model.User;
import org.hardsign.models.auth.TelegramUserMeta;

public class TelegramUserMetaFactory {
    public static TelegramUserMeta create(User user) {
        return new TelegramUserMeta(user.id(), user.username(), user.firstName());
    }
}
