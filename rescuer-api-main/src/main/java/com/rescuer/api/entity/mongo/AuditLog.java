package com.rescuer.api.entity.mongo;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Id;

@Data
@Builder

// @Document
public class AuditLog {

    @Id
    private String id;
    private String identifier;
    private String changeLog;
    private String action;

}
