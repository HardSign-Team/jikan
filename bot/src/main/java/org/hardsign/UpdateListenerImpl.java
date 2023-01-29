package org.hardsign;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.hardsign.clients.JikanApiClient;
import org.hardsign.factories.TimestampsListFactory;
import org.hardsign.factories.UpdateContextFactory;
import org.hardsign.handlers.UpdateHandler;
import org.hardsign.handlers.commands.*;
import org.hardsign.handlers.inputs.*;
import org.hardsign.services.users.UserStateService;
import org.hardsign.handlers.keyboards.*;
import org.hardsign.utils.DateParser;
import org.hardsign.utils.DateParserFromUpdate;
import org.hardsign.utils.TimeFormatter;
import org.hardsign.utils.TimezoneHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class UpdateListenerImpl implements UpdatesListener {
    private static final Logger LOGGER = Logger.getLogger(UpdateListenerImpl.class.getName());
    private final TelegramBot bot;
    private final UpdateContextFactory updateContextFactory;
    private final List<UpdateHandler> updateHandlers = new ArrayList<>();

    public UpdateListenerImpl(
            JikanApiClient jikanApiClient,
            TelegramBot bot,
            UserStateService userStateService) {
        this.bot = bot;
        var timeFormatter = new TimeFormatter();
        var timezoneHelper = new TimezoneHelper();
        var dateParser = new DateParserFromUpdate(new DateParser(), timezoneHelper);
        var timestampsListFactory = new TimestampsListFactory(timeFormatter);

        updateContextFactory = new UpdateContextFactory(jikanApiClient, userStateService);

        updateHandlers.add(new CreateActivityInputHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new CustomDateActivityStatisticsInputHandler(bot, jikanApiClient, userStateService, timeFormatter, dateParser));
        updateHandlers.add(new AddTimestampInputHandler(bot, jikanApiClient, userStateService, dateParser));
        updateHandlers.add(new CustomDateTimestampsInputHandler(bot, jikanApiClient, userStateService, dateParser, timestampsListFactory, timezoneHelper));
        updateHandlers.add(new EditTimestampInputHandler(bot, jikanApiClient, userStateService, dateParser));

        updateHandlers.add(new StartCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new SelectActivityCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new UnselectActivityCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new DeleteActivityCommandHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new DeleteTimestampCommandHandler(bot, jikanApiClient, userStateService, timeFormatter, timezoneHelper));

        updateHandlers.add(new ActivitiesPressHandler(bot, jikanApiClient));
        updateHandlers.add(new TimestampsPressHandler(bot, jikanApiClient, timestampsListFactory, timezoneHelper));
        updateHandlers.add(new AcceptDeleteActivityPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new CreateActivityPressHandler(bot, userStateService));
        updateHandlers.add(new CancelDeleteActivityPressHandler(bot, userStateService));
        updateHandlers.add(new StartPressHandler(bot, jikanApiClient));
        updateHandlers.add(new StopPressHandler(bot, jikanApiClient, timeFormatter));
        updateHandlers.add(new BackPressHandler(bot, userStateService));
        updateHandlers.add(new StatisticsPressHandler(bot));
        updateHandlers.add(new CurrentMonthActivityPressHandler(bot, jikanApiClient, timeFormatter, timezoneHelper));
        updateHandlers.add(new CurrentDayActivityPressHandler(bot, jikanApiClient, timeFormatter, timezoneHelper));
        updateHandlers.add(new CustomDateActivityPressHandler(bot, userStateService));
        updateHandlers.add(new SelectCustomDateForTimestampsButton(bot, userStateService));
        updateHandlers.add(new SinceLastStartActivityPressHandler(bot, timeFormatter));
        updateHandlers.add(new ActivityMenuPressHandler(bot));
        updateHandlers.add(new EditTimestampCommandHandler(bot, jikanApiClient, userStateService, timeFormatter, timezoneHelper));
        updateHandlers.add(new AddTimestampPressHandler(bot, userStateService));
        updateHandlers.add(new AcceptDeleteTimestampPressHandler(bot, jikanApiClient, userStateService));
        updateHandlers.add(new CancelDeleteTimestampPressHandler(bot, userStateService));
    }

    @Override
    public int process(List<Update> list) {
        return list.stream()
                .map(this::process)
                .reduce((x, y) -> y).map(Update::updateId)
                .orElse(CONFIRMED_UPDATES_ALL);
    }

    private Update process(Update update) {
        var message = update.message();
        if (message == null)
            return update;

        var content = message.text();
        if (Objects.equals(content, ""))
            return update;

        var user = message.from();
        var context = updateContextFactory.create(user);

        var exceptionThrown = false;
        for (var handler : updateHandlers) {
            try {
                handler.handle(update, context);
            } catch (Exception e) {
                exceptionThrown = true;
                LOGGER.severe(e.getMessage());
            }
        }

        if (exceptionThrown) {
            sendErrorMessage(message.chat().id());
        }

        return update;
    }

    private void sendErrorMessage(Long chatId) {
        bot.execute(new SendMessage(chatId, "Произошла ошибка :( Попробуй ещё раз!"));
    }
}