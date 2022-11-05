package org.hardsign.models.errors;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Data
public class SpringErrorDto {
    private Date timestamp;
    private int status;
    private String error;
    @Nullable
    private String message;
    private String path;
}
