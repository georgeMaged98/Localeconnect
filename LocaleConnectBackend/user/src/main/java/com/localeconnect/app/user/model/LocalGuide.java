package com.localeconnect.app.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "localguide")
@Getter
@Setter
@NoArgsConstructor
public class LocalGuide extends User {

    @ElementCollection
    private List<String> languages = new ArrayList<>();
    @Column
    private boolean acceptsConditions;

    public LocalGuide(String firstName, String lastName, String userName, String email, LocalDate dateOfBirth, String bio, String password, List<String> languages, boolean acceptsConditions) {
        super(firstName, lastName, userName, email, dateOfBirth, bio, password);
        this.languages = languages;
        this.acceptsConditions = acceptsConditions;
    }

    private boolean isAdult(){
        Period age = Period.between(this.getDateOfBirth(), LocalDate.now());
        return age.getYears() >= 18;
    }

}
