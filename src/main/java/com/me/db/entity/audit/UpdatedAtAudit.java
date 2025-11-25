package com.me.db.entity.audit;

import java.time.LocalDateTime;

public interface UpdatedAtAudit {

  UpdatedAtAudit setModifiedAt(LocalDateTime time);

  boolean isWasModifiedByStrictAction();

}
