package com.localeconnect.app.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class TravelerDTO extends UserDTO {

    private List<String> interests = new ArrayList<>();
    private String travelStyle;
    public TravelerDTO() {
        super();
    }
}
