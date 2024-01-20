package com.localeconnect.app.meetup.controller;

import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.response_handler.ResponseHandler;
import com.localeconnect.app.meetup.service.MeetupService;
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
    public ResponseEntity<Object> createMeetup(@RequestBody @Valid MeetupDTO meetupDTO) {
        System.out.println("------------------------HHHHHHHHH------------------------");
        MeetupDTO createdMeetupDTO = meetupService.createMeetup(meetupDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, createdMeetupDTO, null);
    }


    @GetMapping("/")
    public ResponseEntity<Object> getAllMeetups() {

        List<MeetupDTO> meetupDTOS = meetupService.getAllMeetups();

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, meetupDTOS, null);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getMeetupById(@PathVariable("id") Long id) {

        MeetupDTO meetupDTO = meetupService.getMeetupById(id);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, meetupDTO, null);
    }


    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteMeetupById(@PathVariable("id") Long id) {
        MeetupDTO deletedMeetupDTO = meetupService.deleteMeetupById(id);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, deletedMeetupDTO, null);
    }
}
