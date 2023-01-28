package org.hardsign.models.timestamps;

import lombok.AllArgsConstructor;
import org.hardsign.models.SortType;


@AllArgsConstructor
public class TimestampSortField {
    private TimestampField field;
    private SortType sortType;

    public TimestampSortField(TimestampField field) {
        this(field, SortType.Ascending);
    }
}