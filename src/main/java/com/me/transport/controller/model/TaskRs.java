package com.me.transport.controller.model;

import java.time.LocalDateTime;

public record TaskRs(Long id,
                     String title,
                     String description,
                     PersonRs assignedBy,
                     LocalDateTime lastCreatedAt,
                     LocalDateTime lastUpdatedAt,
                     PersonRs lastCreatedBy,
                     PersonRs lastUpdatedBy) {

}
