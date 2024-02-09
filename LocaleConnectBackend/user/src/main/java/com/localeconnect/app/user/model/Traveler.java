package com.localeconnect.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter
@Setter
@SuperBuilder
public class Traveler extends User {
    private String role = "Traveler";
    @Column(nullable = false)
    private final boolean registeredAsLocalGuide = false;
    public Traveler() {
        super();
    }
}
