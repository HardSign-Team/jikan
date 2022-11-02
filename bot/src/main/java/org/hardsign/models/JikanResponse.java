package org.hardsign.models;

import org.jetbrains.annotations.Nullable;

public class JikanResponse<T> {
    @Nullable
    private final T value;
    private final int code;
    @Nullable
    private final String error;

    public JikanResponse(T value, int code) {
        this(value, code, null);
    }

    public JikanResponse(int code, String error) {
        this(null, code, error);
    }

    private JikanResponse(@Nullable T value, int code, @Nullable String error) {
        this.value = value;
        this.code = code;
        this.error = error;
    }

    @Nullable
    public T getValue() throws Exception {
        if (error != null)
            throw new Exception(error);
        return value;
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
