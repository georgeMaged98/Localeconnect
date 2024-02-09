package com.localeconnect.app.user.dto;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TravelerDTO extends UserDTO {
    private String role = "Traveler";

    private boolean registeredAsLocalGuide = false;
    public TravelerDTO() {
        super();
    }
}
