package com.me.transport.controller.model;

import jakarta.validation.constraints.NotEmpty;

public record PersonRq(@NotEmpty String name) {

}
