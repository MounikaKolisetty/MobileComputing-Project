package com.rescuer.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Embeddable;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Attachment {

    private String URL;
    private String attachmentName;
    private String attachmentType;
    private Binary file;
}
