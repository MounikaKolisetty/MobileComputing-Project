package com.rescuer.api.dtos.requestDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInputDto {

    private String userName;
    private String password;
    private Boolean isUserActive;
}
