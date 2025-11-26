package com.me.db.entity.audit;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.me.db.entity.PersonEntity;
import com.me.db.service.SystemUserProvider;
import com.me.mapper.JpaRefMapper;
import com.me.util.SecurityUtils;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @Lazy)
public class UserAuditAware {

  private SystemUserProvider systemUserProvider;
  private JpaRefMapper jpaRefMapper;

  @PrePersist
  private void setCreator(CreatedByAudit audit) {
    if (audit.getCreatedBy() == null) {
      SecurityUtils.findAuthContext().ifPresentOrElse(
          user -> audit.setCreator(jpaRefMapper.createProxy(user.localId(), PersonEntity.class)),
          () -> audit.setCreator(jpaRefMapper.createProxy(systemUserProvider.getSystemId(), PersonEntity.class))
      );
    }
  }

  @PreUpdate
  private void setUpdater(UpdatedByAudit audit) {
    if (!audit.isNew() && audit.isWasModifiedByStrictAction()) {
        SecurityUtils.findAuthContext().ifPresentOrElse(
            user -> audit.setUpdater(jpaRefMapper.createProxy(user.localId(), PersonEntity.class)),
            () -> audit.setUpdater(jpaRefMapper.createProxy(systemUserProvider.getSystemId(), PersonEntity.class))
        );
    }
  }

}
