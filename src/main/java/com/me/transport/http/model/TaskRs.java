package com.me.transport.http.model;

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
