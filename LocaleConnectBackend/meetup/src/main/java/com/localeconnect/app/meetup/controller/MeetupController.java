package com.localeconnect.app.meetup.controller;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.service.MeetupService;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<MeetupDTO>> getAllMeetups(){
        try {
            List<MeetupDTO> meetupDTOS = meetupService.getAllMeetups();

            return ResponseEntity.ok(meetupDTOS);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MeetupDTO> getMeetupById(@PathVariable("id") Long id ){
        try {
            MeetupDTO meetupDTO = meetupService.getMeetupById(id);
            return ResponseEntity.ok(meetupDTO);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteMeetupById(@PathVariable("id") Long id ){
        try {
            meetupService.deleteMeetupById(id);
            return ResponseEntity.ok("Meetup Deleted Successfully!");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
