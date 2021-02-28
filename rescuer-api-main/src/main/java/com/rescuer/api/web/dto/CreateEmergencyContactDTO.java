package com.rescuer.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEmergencyContactDTO {

    private String emergencyContactName;
    private int emergencyContactAge;
    private String emergencyPhoneNumber;
    private String emergencyAddress;
}
