package org.hardsign.factories;

import org.hardsign.handlers.commands.DeleteActivityCommandHandler;
import org.hardsign.handlers.commands.SelectActivityCommandHandler;
import org.hardsign.handlers.commands.UnselectActivityCommandHandler;
import org.hardsign.models.Emoji;
import org.hardsign.models.UpdateContext;
import org.hardsign.models.activities.ActivityDto;
import org.hardsign.models.activities.ActivityOverviewDto;

import java.util.Optional;

public class ActivitiesListFactory {
    public String create(ActivityOverviewDto[] activities, UpdateContext context) {
        if (activities.length == 0)
            return "У тебя еще нет активностей. Можешь добавить их :)";

        long currentActivityId = Optional.ofNullable(context.getActivity())
                .map(ActivityDto::getId)
                .orElse(0L);

        var newLine = System.lineSeparator();
        var sb = new StringBuilder();
        for (var i = 0; i < activities.length; i++) {
            var activity = activities[i];
            sb.append(i + 1).append(". ");
            if (currentActivityId == activity.getId()) {
                sb.append(Emoji.GreenCircle).append(' ');
                appendName(sb, activity);
                appendStatus(sb, activity).append(newLine);
                appendUnselectCommand(sb, activity).append(newLine);
            } else {
                sb.append(Emoji.YellowCircle).append(' ');
                appendName(sb, activity);
                appendStatus(sb, activity).append(newLine);
                appendSelectCommand(sb, activity).append(newLine);
                appendDeleteCommand(sb, activity).append(newLine);
            }
            sb.append(newLine);
        }
        return sb.toString();
    }


    private void appendName(StringBuilder sb, ActivityOverviewDto activity) {
        sb.append(activity.getName()).append('.');
    }

    private StringBuilder appendStatus(StringBuilder sb, ActivityOverviewDto activity) {
        if (activity.getActiveTimestamp() != null) {
            return sb.append(' ').append(Emoji.Clock1);
        }
        return sb;
    }

    private StringBuilder appendSelectCommand(StringBuilder sb, ActivityOverviewDto activity) {
        var command = SelectActivityCommandHandler.create(activity.getId());
        return sb.append(Emoji.WhiteQuestion).append(' ').append("Выбрать: ").append(command);
    }

    private StringBuilder appendUnselectCommand(StringBuilder sb, ActivityOverviewDto activity) {
        var command = UnselectActivityCommandHandler.create(activity.getId());
        return sb.append(Emoji.ArrowsCircle).append(' ').append("Отменить: ").append(command);
    }

    private StringBuilder appendDeleteCommand(StringBuilder sb, ActivityOverviewDto activityDto) {
        var command = DeleteActivityCommandHandler.create(activityDto.getId());
        return sb.append(Emoji.TrashCan).append(' ').append("Удалить: ").append(command);
    }
}
