package com.localeconnect.app.meetup.controller;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.service.MeetupService;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/meetup")
public class MeetupController {

    private final MeetupService meetupService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MeetupDTO> createMeetup(@RequestBody @Valid MeetupDTO meetupDTO){
        try {
            MeetupDTO createdMeetupDTO = meetupService.createMeetup(meetupDTO);

            return ResponseEntity.ok(createdMeetupDTO);
        }catch (IllegalArgumentException | UnexpectedTypeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> getAllMeetups(){
        try {
            return ResponseEntity.ok("LOL");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}
