package com.localeconnect.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Traveler extends User {
    @ElementCollection
    private List<String> interests = new ArrayList<>();
    @Column
    private String travelStyle;

    public Traveler() {
        super();
    }
}
