package com.rescuer.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Audit {

    @CreatedDate
    private Instant createdAt = Instant.now();
    @LastModifiedDate
    private Instant modifiedAt = Instant.now();
    @LastModifiedBy
    private String modifiedBy;
}
