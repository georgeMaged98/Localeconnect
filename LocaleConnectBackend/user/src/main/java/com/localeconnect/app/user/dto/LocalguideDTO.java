package com.localeconnect.app.user.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class LocalguideDTO extends UserDTO {

    private String city;
    private double rating;
    public LocalguideDTO() {
        super();
    }
}
