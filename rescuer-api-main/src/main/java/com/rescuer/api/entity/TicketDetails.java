package com.rescuer.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetails {

    private Victim victim;
    private EmergencyContact emergencyContact;
    private int victimsAroundCount;
    private String additionalInformation;
}
