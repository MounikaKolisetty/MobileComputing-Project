package com.rescuer.api.repository.audit;

import com.rescuer.api.entity.mongo.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends MongoRepository<AuditLog, String> {
}
