package org.hardsign.handlers.inputs;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.factories.KeyboardFactory;
import org.hardsign.factories.TimestampsListFactory;
import org.hardsign.handlers.BaseUpdateHandler;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.users.State;
import org.hardsign.services.timestamps.TimestampsService;
import org.hardsign.services.users.UserStateService;
import org.hardsign.utils.DateParserFromUpdate;
import org.hardsign.utils.MessagesHelper;
import org.hardsign.utils.TelegramUtils;
import org.hardsign.utils.TimezoneHelper;

public class CustomDateTimestampsInputHandler extends BaseUpdateHandler implements InputHandler {
    private final TelegramBot bot;
    private final TimestampsService timestampsService;
    private final UserStateService userStateService;
    private final DateParserFromUpdate dateParser;
    private final TimestampsListFactory timestampsListFactory;
    private final TimezoneHelper timezoneHelper;

    public CustomDateTimestampsInputHandler(
            TelegramBot bot,
            TimestampsService timestampsService,
            UserStateService userStateService,
            DateParserFromUpdate dateParser,
            TimestampsListFactory timestampsListFactory,
            TimezoneHelper timezoneHelper) {
        this.bot = bot;
        this.timestampsService = timestampsService;
        this.userStateService = userStateService;
        this.dateParser = dateParser;
        this.timestampsListFactory = timestampsListFactory;
        this.timezoneHelper = timezoneHelper;
    }

    @Override
    protected State requiredState() {
        return State.SelectCustomDateRangeTimestamps;
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

        userStateService.with(context).setState(user, State.None);

        var timestamps = timestampsService.findTimestamps(activity.getId(), dateRange.get(), context.getMeta());

        var zone = timezoneHelper.getZone(update.message().location());
        var text = timestampsListFactory.create(activity, timestamps, zone);
        var keyboard = KeyboardFactory.createTimestampsMenu();
        bot.execute(new SendMessage(chatId, text).replyMarkup(keyboard).parseMode(TelegramUtils.PARSE_MODE));
    }

    private void sendIncorrectFormatMessage(Long chatId, UpdateContext context) {
        var text = MessagesHelper.createIncorrectDateRangeFormatMessage();
        sendDefaultMenuMessage(bot, context, chatId, text);
    }
}
