package com.rescuer.api.web.dto;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"ticketStatus", "allocatedTo", "createdAt", "requiredItems"})
public class GetAllTicketsDTO {

    private String ticketId;
    private String ticketStatus;
    private String allocatedTo;
    private String createdAt;
    private Set<String> requiredItems;

}
