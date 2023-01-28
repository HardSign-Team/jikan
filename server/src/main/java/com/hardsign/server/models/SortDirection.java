package com.hardsign.server.models;


import org.springframework.data.domain.Sort;

public enum SortDirection {
    Ascending(Sort.Direction.ASC),
    Descending(Sort.Direction.DESC);

    private final Sort.Direction direction;

    SortDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public Sort.Direction value() {
        return direction;
    }
}
