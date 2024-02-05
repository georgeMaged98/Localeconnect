package com.localeconnect.app.user.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class TravelerDTO extends UserDTO {
    public TravelerDTO() {
        super();
    }
}
