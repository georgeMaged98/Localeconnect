package com.localeconnect.app.itinerary.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryAttendDTO {

    @NotNull(message = "This is a required field")
    private Long travellerId;
}

