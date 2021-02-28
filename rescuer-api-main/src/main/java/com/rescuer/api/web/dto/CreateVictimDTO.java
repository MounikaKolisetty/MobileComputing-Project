package com.rescuer.api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVictimDTO {

    private String victimName;
    private int victimAge;
    private Set<String> requiredItems;
    private String latitude;
    private String longitude;
}
