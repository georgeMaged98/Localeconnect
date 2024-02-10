package com.localeconnect.app.itinerary.model;

import com.localeconnect.app.itinerary.dto.Tag;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer numberOfDays;

//    @ElementCollection(targetClass = Tag.class)
//    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "itinerary_tags")
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    private List<String> placesToVisit = new ArrayList<>();

    @ElementCollection
    private List<String> dailyActivities = new ArrayList<>();

    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();
}


