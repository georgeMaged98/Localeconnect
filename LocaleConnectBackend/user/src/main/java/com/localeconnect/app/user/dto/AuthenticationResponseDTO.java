package com.localeconnect.app.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AuthenticationResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    public AuthenticationResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
