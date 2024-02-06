package com.localeconnect.app.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LocalguideDTO extends UserDTO {
    private String city;
    @Min(value = 0)
    @Max(value = 5)
    private double averageRating;

    public LocalguideDTO() {
        super();
    }
}

