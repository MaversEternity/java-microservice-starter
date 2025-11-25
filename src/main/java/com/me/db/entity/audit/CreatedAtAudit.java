package com.me.db.entity.audit;

import java.time.LocalDateTime;

public interface CreatedAtAudit {

  LocalDateTime getCreatedAt();

  CreatedAtAudit setCreatedAt(LocalDateTime time);

}
