package com.localeconnect.app.itinerary.model;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "itinerary")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;
    private String name;
    private String description;
    private int numberOfDays;

    @ElementCollection
    private List<String> tags= new ArrayList<>();

    @ElementCollection
    private List<String> placesToVisit = new ArrayList<>();

    @ElementCollection
    private List<String> dailyActivities= new ArrayList<>();

    //TODO: Images can be stored as URLs or as byte arrays
    @ElementCollection
    private List<String> imageUrls= new ArrayList<>();
}


