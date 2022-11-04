package com.hardsign.server.models.activities.requests;


import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateActivityRequest {

    @NotEmpty
    private String name;
}

