package com.localeconnect.app.feed.dto;

import com.localeconnect.app.feed.error_handler.ErrorResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CheckUserExistsResponseDTO {
    private Boolean responseObject;
    private String message;
    private ErrorResponse errors;
    private int status;
}