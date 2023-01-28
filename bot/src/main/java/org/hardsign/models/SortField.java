package org.hardsign.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SortField<T> {
    private T field;
    private SortDirection direction;
}
