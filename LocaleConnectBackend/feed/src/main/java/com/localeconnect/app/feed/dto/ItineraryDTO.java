package com.localeconnect.app.feed.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItineraryDTO {
    private Long id;
    private Long userId;
    @NotBlank(message = "This is a required field")
    @NotNull
    private String name;
    @NotBlank(message = "This is a required field")
    @NotNull
    private String description;
    @Positive(message = "Number of days must be greater than or equal to 1")
    private Integer numberOfDays;
    private List<String> tags;
    @NotEmpty(message = "Provide at least one destination")
    @NotNull
    private List<String> placesToVisit;
    private List<String> dailyActivities;
    private List<String> imageUrls;

}
