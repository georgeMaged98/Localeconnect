package com.localeconnect.app.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalguideDTO extends UserDTO {

    private String city;
    private double rating;
    public LocalguideDTO() {
        super();
    }
}
