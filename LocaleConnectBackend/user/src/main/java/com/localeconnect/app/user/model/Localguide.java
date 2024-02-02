package com.localeconnect.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@SuperBuilder
public class Localguide extends User {
    @Column
    private String city;
    @Column
    private double rating;
    public Localguide() {
        super();
    }
}
