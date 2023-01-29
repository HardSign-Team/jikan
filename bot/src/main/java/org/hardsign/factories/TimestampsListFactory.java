package org.hardsign.factories;

import org.hardsign.handlers.commands.DeleteTimestampCommandHandler;
import org.hardsign.handlers.commands.EditTimestampCommandHandler;
import org.hardsign.models.Emoji;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.timestamps.TimestampDto;
import org.hardsign.utils.TimeFormatter;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.util.Optional;

import static java.lang.System.lineSeparator;
import static org.hardsign.utils.TelegramUtils.bold;

public class TimestampsListFactory {
    private final TimeFormatter formatter;

    public TimestampsListFactory(TimeFormatter formatter) {
        this.formatter = formatter;
    }

    @NotNull
    public String create(ActivityDto activity, TimestampDto[] timestamps, ZoneId zone) {
        if (timestamps.length == 0) {
            return "Вы еще не фиксировали активность :)";
        }

        var sb = new StringBuilder();
        sb.append("Последние фиксации активности ").append(bold(activity.getName())).append(':').append(lineSeparator());

        for (var i = 0; i < timestamps.length; i++) {
            var str = createTimestampDescription(i + 1, timestamps[i], zone);
            sb.append(str).append(lineSeparator());
        }

        return sb.toString();
    }

    @NotNull
    private String createTimestampDescription(int position, TimestampDto timestamp, ZoneId zone) {
        var timestampSb = new StringBuilder();
        var start = formatter.format(timestamp.getStart().atZone(zone));
        var end = Optional.ofNullable(timestamp.getEnd()).map(x -> x.atZone(zone)).map(formatter::format).orElse("...");

        timestampSb.append(Emoji.Clock1).append(' ');
        timestampSb.append(position).append(". ").append(bold(start)).append(" — ").append(bold(end)).append(lineSeparator());
        timestampSb.append("Редактировать: ").append(EditTimestampCommandHandler.create(timestamp.getId())).append(lineSeparator());
        timestampSb.append("Удалить: ").append(DeleteTimestampCommandHandler.create(timestamp.getId())).append(lineSeparator());

        return timestampSb.toString();
    }
}
