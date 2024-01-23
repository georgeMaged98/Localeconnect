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
public class Localguide extends User {
    @Column
    private String city;
    @Column
    private double rating;
    public Localguide() {
        super();
    }
}
