package com.hardsign.server.models.activities.requests;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateActivityRequest {

    @NotBlank
    private String name;
}

