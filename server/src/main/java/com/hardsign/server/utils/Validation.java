package com.hardsign.server.utils;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.function.Function;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Validation<T> {
    @Nullable
    private T value;
    @Nullable
    private String reason;

    public static <T> Validation<T> invalid(String reason) {
        return new Validation<>(null, reason);
    }

    public static <T> Validation<T> valid(T value) {
        return new Validation<>(value, null);
    }

    public <TResult> Validation<TResult> map(Function<T, TResult> mapper) {
        return value != null
                ? Validation.valid(mapper.apply(value))
                : Validation.invalid(reason);
    }

    public <TException extends Exception> T orElseThrow(Function<String, ? extends TException> exceptionProvider)
            throws TException {
        if (value == null) {
            throw exceptionProvider.apply(reason);
        }
        return value;
    }
}
