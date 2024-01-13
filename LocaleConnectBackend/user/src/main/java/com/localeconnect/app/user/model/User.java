package com.localeconnect.app.user.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    @Column
    private String bio;
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private String password;
    @ElementCollection
    private List<String> visitedCountries = new ArrayList<>();
    @Column
    private boolean registeredAsLocalGuide;
    @ElementCollection
    private List<String> languages;

}
