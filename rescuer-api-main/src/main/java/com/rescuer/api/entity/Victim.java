package com.rescuer.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class Victim {

    private String victimName;
    private int victimAge;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "victim_mandatory_items")
    private Set<String> requiredItems;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "victim_attachments")
    private Set<Attachment> attachments;
    private String reportedLocation;
}
