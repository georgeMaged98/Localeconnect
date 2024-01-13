package com.localeconnect.app.meetup.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meetup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meetup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator_id")
    private Long creatorId;
    private String name;
    private String description;
    private Date date;
    private String from;
    private String to;
    private double cost;
    private String location;

    @ElementCollection
    private List<String> spokenLanguages= new ArrayList<>();

}
