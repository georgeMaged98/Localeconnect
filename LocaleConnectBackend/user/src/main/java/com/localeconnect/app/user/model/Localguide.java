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
    private double ratingsTotal;

    @Column
    private int ratingsCount;

    @Column
    private double averageRating;
    private String role = "Localguide";

    @Column(nullable = false)
    private final boolean registeredAsLocalGuide = true;

    public Localguide() {
        super();
    }

}

