package com.localeconnect.app.user.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
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

    // Custom constructor to initialize all fields except Id as it is generated
    public User(String firstName, String lastName, String userName, String email, LocalDate dateOfBirth, String bio, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.password = password;
    }

}
