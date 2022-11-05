package org.hardsign.models;

public enum HttpCodes {
    NotFound(404);

    private final int code;

    HttpCodes(int code) {
        this.code = code;
    }

    public boolean is(int code) {
        return this.code == code;
    }
}
