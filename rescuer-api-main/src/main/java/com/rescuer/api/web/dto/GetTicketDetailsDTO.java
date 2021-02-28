package com.rescuer.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetTicketDetailsDTO {
    private String ticketId;
    private String ticketStatus;
    private String allocatedTo;
    private String createdAt;
    private Set<String> requiredItems;
    private String latitude;
    private String longitude;
    private String additionalInformation;
    private int victimsAround;
    private GetVictimDetailsDTO victimDetails;
    private GetEmergencyContactDetails emergencyContactDetails;

}
