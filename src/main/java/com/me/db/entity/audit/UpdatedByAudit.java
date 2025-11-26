package com.me.db.entity.audit;

import com.me.db.entity.PersonEntity;

/**
 * User audit
 */
public interface UpdatedByAudit {

    UpdatedByAudit setUpdater(PersonEntity updater);

    boolean isWasModifiedByStrictAction();

    boolean isNew();
}
