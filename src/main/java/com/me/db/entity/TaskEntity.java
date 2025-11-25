package com.me.db.entity;

import java.util.Optional;

import com.me.common.TaskState;
import com.me.db.entity.audit.UpdatableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Setter
@Entity
@Table(name = "task")
public class TaskEntity extends UpdatableEntity {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskState state = TaskState.NEW;

    @Getter(value = AccessLevel.PRIVATE)
    @Column(name = "assigned_by", updatable = false, insertable = false)
    protected Long assignedBy;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", referencedColumnName = "id", updatable = false)
    private PersonEntity assignee;

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    public Optional<Long> findAssignedBy() {
        return Optional.ofNullable(getAssignedBy());
    }

}
