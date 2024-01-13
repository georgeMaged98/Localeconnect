package com.localeconnect.app.meetup.dto;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetupDTO {

    private Long id;

    @NotBlank(message = "This is a required field")
    private Long creatorId;

    @NotBlank(message = "This is a required field")
    private String name;

    @NotBlank(message = "This is a required field")
    private String description;

    @NotBlank(message = "This is a required field")
    private Date date;

    @NotBlank(message = "This is a required field")
    private String from;

    @NotBlank(message = "This is a required field")
    private String to;

    @NotBlank(message = "This is a required field")
    private double cost;

    @NotBlank(message = "This is a required field")
    private String location;

    private List<String> spokenLanguages= new ArrayList<>();

    private List<Long> meetupAttendees = new ArrayList<>();
}
