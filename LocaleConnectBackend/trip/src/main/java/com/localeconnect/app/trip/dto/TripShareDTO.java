package com.localeconnect.app.trip.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripShareDTO {
    private Long id;
    @NotBlank(message = "This is a required field")
    private String name;
    private String description;
}