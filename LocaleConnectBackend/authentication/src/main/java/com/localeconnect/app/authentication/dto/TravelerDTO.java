package com.localeconnect.app.authentication.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TravelerDTO extends UserDTO {
    public TravelerDTO() {
        super();
    }
}
