package com.localeconnect.app.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@Getter
public class AuthenticationResponseDTO {
    private String token;


}
