package com.localeconnect.app.authentication.dto;

import lombok.Getter;
import lombok.Setter;
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
