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

    public Localguide() {
        super();
    }

    public void addRating(double rating) {
        this.ratingsTotal += rating;
        this.ratingsCount++;
    }

    public double getAverageRating() {
        return (this.ratingsCount > 0) ? this.ratingsTotal / this.ratingsCount : 0;
    }
}

