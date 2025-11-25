package com.me.transport.controller.model;

import jakarta.validation.constraints.NotEmpty;

public record TaskRq(@NotEmpty String title, String description, Long assignedBy) {

}
