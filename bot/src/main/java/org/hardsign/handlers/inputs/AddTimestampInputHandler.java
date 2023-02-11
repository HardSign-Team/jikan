package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.users.State;
import org.hardsign.services.TimestampsService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.DateParserFromUpdate;
import org.hardsign.utils.MessagesHelper;

public class AddTimestampInputHandler extends BaseUpdateHandler implements InputHandler {

    private final TelegramBot bot;
    private final TimestampsService timestampsService;
    private final UserStateService userStateService;
    private final DateParserFromUpdate dateParser;

    public AddTimestampInputHandler(
            TelegramBot bot,
            TimestampsService timestampsService,
            UserStateService userStateService,
            DateParserFromUpdate dateParser) {
        this.bot = bot;
        this.timestampsService = timestampsService;
        this.userStateService = userStateService;
        this.dateParser = dateParser;
    }

    @Override
    protected void handleInternal(User user, Update update, UpdateContext context) throws Exception {
        var chatId = update.message().chat().id();
        var activity = context.getActivity();

        if (activity == null) {
            handleNoCurrentActivity(bot, context, chatId);
            return;
        }

        var dateRange = dateParser.parseDateRange(update);
        if (dateRange.isEmpty()) {
            sendIncorrectFormatMessage(chatId, context);
            return;
        }

        var result = timestampsService.addTimestamp(activity.getId(), dateRange.get(), context.getMeta());
        if (result.isConflicted()) {
            sendConflictMessage(chatId, context);
            return;
        }

        userStateService.with(context).setState(user, State.None);

        sendSuccessMessage(update, context, chatId, activity);
    }

    private void sendSuccessMessage(Update update, UpdateContext context, Long chatId, ActivityDto activity) {
        var input = update.message().text();
        var text = "Добавлена фиксация времени '" + input + "' для активности '" + activity.getName() + "'.";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void sendConflictMessage(Long chatId, UpdateContext context) {
        var text = "Введенный период пересекается с другой фиксацией. Пожалуйста, измени период :o";
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    private void sendIncorrectFormatMessage(Long chatId, UpdateContext context) {
        var text = MessagesHelper.createIncorrectDateRangeFormatMessage();
        sendDefaultMenuMessage(bot, context, chatId, text);
    }

    @Override
    protected State requiredState() {
        return State.AddTimestampDateRange;
    }
}
