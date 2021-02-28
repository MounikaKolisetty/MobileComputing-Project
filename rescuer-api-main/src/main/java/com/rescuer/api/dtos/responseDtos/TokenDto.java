package com.rescuer.api.dtos.responseDtos;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto implements Serializable {
    private String tokenId;
    private String refreshToken;


    @SneakyThrows
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        return mapper.writeValueAsString(this);
    }
}
