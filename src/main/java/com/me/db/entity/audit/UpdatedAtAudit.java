package com.me.db.entity.audit;

import java.time.LocalDateTime;

public interface UpdatedAtAudit {

    UpdatedAtAudit setUpdatedAt(LocalDateTime time);

    boolean isWasModifiedByStrictAction();

}
