package org.hardsign.models;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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

    public Optional<T> getValue() {
        if (isFail())
            return Optional.empty();
        return Optional.ofNullable(this.value);
    }

    public T getValueOrThrow() throws Exception {
        ensureSuccess();
        if (value == null)
            throw new Exception("Value was null");
        return value;
    }

    public void ensureSuccess() throws Exception {
        if (isFail())
            throw new Exception("Jikan response with code " + code + ". Error: " + error);
    }

    public int getCode() {
        return code;
    }

    @Nullable
    public String getError() {
        return error;
    }

    public boolean isFail() {
        return error != null;
    }

    public boolean isSuccess() { return error == null; }
}
