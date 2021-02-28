package com.rescuer.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class EmergencyContact {

    private String emergencyContactName;
    private int emergencyContactAge;
    private String emergencyPhoneNumber;
    private String emergencyAddress;

}
