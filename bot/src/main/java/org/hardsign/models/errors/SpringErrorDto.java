package org.hardsign.models.errors;

import lombok.Data;

import java.util.Date;

@Data
public class SpringErrorDto {
    private Date timestamp;
    private int status;
    private String error;
    private String path;
}
