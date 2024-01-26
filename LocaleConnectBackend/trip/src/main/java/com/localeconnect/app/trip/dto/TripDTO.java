package com.localeconnect.app.trip.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDTO {

    private Long localguideId;

    @NotBlank(message = "Trip name is required")
    private String name;

    private String description;

    @FutureOrPresent(message = "Departure time cannot be in the past")
    private LocalDate departureTime;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Duration is required")
    private LocalDate duration;

    @Positive(message = "A trip must have at least one traveler")
    private Integer capacity;

    @NotEmpty(message = "At least one traveler must be in the trip")
    @ElementCollection
    private List<Long> travelers = new ArrayList<>();

    @NotEmpty(message = "At least one language must be specified")
    @ElementCollection
    private List<String> languages = new ArrayList<>();

    @NotEmpty(message = "At least one place to visit must be specified")
    @ElementCollection
    private List<String> placesToVisit = new ArrayList<>();

    @ElementCollection
    private List<String> dailyActivities = new ArrayList<>();

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

}
