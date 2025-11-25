package com.me.db.entity.audit;

import com.me.common.CurrentTimeProvider;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class DateTimeAuditAware {

  @PrePersist
  private void setCreatedAt(CreatedAtAudit audit) {
    if (audit.getCreatedAt() == null) {
        audit.setCreatedAt(CurrentTimeProvider.now());
    }
  }

  @PreUpdate
  private void setModifiedAt(UpdatedAtAudit audit) {
    if (audit.isWasModifiedByStrictAction()) {
        audit.setModifiedAt(CurrentTimeProvider.now());
    }
  }

}
