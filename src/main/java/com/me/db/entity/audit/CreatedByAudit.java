package com.me.db.entity.audit;

import com.me.db.entity.PersonEntity;

/**
 * User audit
 */
public interface CreatedByAudit {

  Long getCreatedBy();

  CreatedByAudit setCreator(PersonEntity creator);
}
