package com.rescuer.api.repository.audit;

import com.rescuer.api.entity.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, String> {
}
