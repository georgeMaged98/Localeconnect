package com.localeconnect.app.meetup.controller;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.model.Meetup;
import com.localeconnect.app.meetup.service.MeetupService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meetup")
@AllArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;

//    public ResponseEntity<Meetup> createMeetup(@RequestBody MeetupDTO meetupDTO){
//
//    }
}
