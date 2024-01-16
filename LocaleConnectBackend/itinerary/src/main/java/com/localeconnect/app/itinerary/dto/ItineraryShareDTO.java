package com.localeconnect.app.itinerary.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryShareDTO {
    private Long id;
    @NotBlank(message = "This is a required field")
    private String name;
    private String description;
}
