package com.localeconnect.app.user.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "traveler")
@Getter
@Setter
@NoArgsConstructor
public class Traveler extends User {

    @ElementCollection
    private List<String> visitedCountries = new ArrayList<>();

    // Custom constructor to initialize all fields including those from User
    public Traveler(String firstName, String lastName, String userName, String email, LocalDate dateOfBirth, String bio, String password, List<String> visitedCountries) {
        super(firstName, lastName, userName, email, dateOfBirth, bio, password); // Initialize fields from User
        this.visitedCountries = visitedCountries; // Initialize Traveler-specific field
    }

}
