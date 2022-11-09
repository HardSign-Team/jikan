package org.hardsign.models;

public enum HttpCodes {
    Ok(200),
    NotFound(404),
    ;

    private final int code;

    HttpCodes(int code) {
        this.code = code;
    }

    public boolean is(int code) {
        return this.code == code;
    }
}
