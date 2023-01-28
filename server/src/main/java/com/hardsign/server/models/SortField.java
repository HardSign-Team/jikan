package com.hardsign.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SortField<T extends FieldName> {
    private T field;
    private SortDirection direction;

    public Sort.Order toSortOrder() {
        return new Sort.Order(direction.value(), field.getFieldName());
    }
}
