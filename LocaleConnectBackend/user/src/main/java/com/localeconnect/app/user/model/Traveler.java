package com.localeconnect.app.user.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
@Entity
@Getter
@Setter
@SuperBuilder
public class Traveler extends User {
    public Traveler() {
        super();
    }
}
