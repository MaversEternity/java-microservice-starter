package com.me.db.entity.audit;

import java.time.LocalDateTime;

import com.me.db.entity.IdEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners({ DateTimeAuditAware.class })
@ToString
public abstract class UpdatableTimeEntity implements
    IdEntity<Long>,
    CreatedAtAudit,
    UpdatedAtAudit
{

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Transient
    private boolean wasModifiedByStrictAction = true;

    @Override
    public UpdatableTimeEntity setUpdatedAt(LocalDateTime time) {
        updatedAt = time;
        return this;
    }

    @Override
    public UpdatableTimeEntity setCreatedAt(LocalDateTime time) {
        createdAt = time;
        return this;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean isWasModifiedByStrictAction() {
        return wasModifiedByStrictAction;
    }
}
