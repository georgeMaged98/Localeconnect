package com.localeconnect.app.trip.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "localguide_id", nullable = false, unique = true)
    private Long localguideId;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    private LocalDate departureTime;
    private String destination;
    private Double travelTime;
    private Integer capacity;

    @ElementCollection
    private List<Long> travelers = new ArrayList<>();

    @ElementCollection
    private List<String> languages = new ArrayList<>();

    @ElementCollection
    private List<String> dailyActivities = new ArrayList<>();

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();

    @Column
    private double ratingsTotal;

    @Column
    private int ratingsCount;

    @Column
    private double averageRating;
    public void addRating(double rating) {
        this.ratingsTotal += rating;
        this.ratingsCount++;
    }
    public void calcAverageRating() {
        this.averageRating = (this.ratingsCount > 0) ? this.ratingsTotal / this.ratingsCount : 0;
    }


}
