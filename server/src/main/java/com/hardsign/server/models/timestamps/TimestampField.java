package com.hardsign.server.models.timestamps;

import com.hardsign.server.models.FieldName;

public enum TimestampField implements FieldName {
    ID("id"),
    START("start"),
    END("end");


    private final String name;

    TimestampField(String name) {
        this.name = name;
    }

    @Override
    public String getFieldName() {
        return name;
    }
}
