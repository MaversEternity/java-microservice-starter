package com.me.transport.http.model;

import jakarta.validation.constraints.NotEmpty;

public record PersonRq(@NotEmpty String name) {

}
