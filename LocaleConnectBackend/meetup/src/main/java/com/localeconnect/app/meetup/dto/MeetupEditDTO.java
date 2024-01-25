package com.localeconnect.app.meetup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetupEditDTO {


    private String name;
    private String description;
    private Date date;
    private String startTime;
    private String endTime;
    private double cost;
    private String location;
    private List<String> spokenLanguages= new ArrayList<>();

}


