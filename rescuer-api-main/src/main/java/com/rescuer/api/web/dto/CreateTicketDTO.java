package com.rescuer.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTicketDTO {

    private CreateEmergencyContactDTO emergencyDetails;
    private CreateVictimDTO victimDetails;
    private int victimsAround;
    private String additionalInformation;
}
