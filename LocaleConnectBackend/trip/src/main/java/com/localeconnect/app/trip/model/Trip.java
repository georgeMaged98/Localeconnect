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
    @Column(name = "localguide_id")
    private Long localguideId;
    private String name;
    private String description;
    private LocalDate departureTime;
    private String destination;
    private LocalDate duration;
    private Integer capacity;
    @ElementCollection
    private List<String> languages = new ArrayList<>();
    @ElementCollection
    private List<String> placesToVisit = new ArrayList<>();

    @ElementCollection
    private List<String> dailyActivities = new ArrayList<>();

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();


}
