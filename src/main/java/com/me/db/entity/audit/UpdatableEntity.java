package com.me.db.entity.audit;

import java.time.LocalDateTime;
import java.util.Optional;

import com.me.db.entity.IdEntity;
import com.me.db.entity.PersonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners({ DateTimeAuditAware.class, UserAuditAware.class })
@ToString
public abstract class UpdatableEntity implements
    IdEntity<Long>,
    CreatedAtAudit,
    UpdatedAtAudit,
    CreatedByAudit,
    UpdatedByAudit
{

    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "created_by", updatable = false, insertable = false)
    protected Long createdBy;

    @Getter(value = AccessLevel.PRIVATE)
    @Column(name = "updated_by", updatable = false, insertable = false)
    protected Long updatedBy;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id", updatable = false)
    private PersonEntity creator;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private PersonEntity updater;

    @Transient
    private boolean wasModifiedByStrictAction = true;

    public Optional<Long> findUpdatedBy() {
        return Optional.ofNullable(getUpdatedBy());
    }

    @Override
    public UpdatableEntity setUpdatedAt(LocalDateTime time) {
        updatedAt = time;
        return this;
    }

    @Override
    public UpdatableEntity setCreatedAt(LocalDateTime time) {
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
