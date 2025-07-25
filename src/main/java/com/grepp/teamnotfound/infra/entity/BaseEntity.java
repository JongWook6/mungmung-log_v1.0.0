package com.grepp.teamnotfound.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.OffsetDateTime;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(nullable = false)
    protected OffsetDateTime createdAt = OffsetDateTime.now();

    protected OffsetDateTime updatedAt;

    protected OffsetDateTime deletedAt;

}
