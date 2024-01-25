package com.localeconnect.app.meetup.controller;

import com.localeconnect.app.meetup.dto.MeetupAttendDTO;
import com.localeconnect.app.meetup.dto.MeetupDTO;
import com.localeconnect.app.meetup.dto.MeetupEditDTO;
import com.localeconnect.app.meetup.response_handler.ResponseHandler;
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
    public ResponseEntity<Object> createMeetup(@RequestBody @Valid MeetupDTO meetupDTO) {
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


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMeetup(@PathVariable("id") Long id, @RequestBody MeetupEditDTO meetupEditDTO) {
        MeetupDTO updatedMeetup = meetupService.updateMeetup(meetupEditDTO, id);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, updatedMeetup, null);

    }

    @PostMapping("/{id}/attend")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> attendMeetup(@PathVariable("id") Long meetupId, @RequestBody @Valid MeetupAttendDTO meetupAttendDTO) {

        meetupService.attendMeetup(meetupId, meetupAttendDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, "Traveller added to meetup attendees successfully!", null);
    }

    @PostMapping("/{id}/unattend")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> unattendMeetup(@PathVariable("id") Long meetupId, @RequestBody @Valid MeetupAttendDTO meetupAttendDTO) {

        meetupService.unattendMeetup(meetupId, meetupAttendDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, "Traveller removed from meetup attendees successfully!", null);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Object> deleteMeetupById(@PathVariable("id") Long id) {
        MeetupDTO deletedMeetupDTO = meetupService.deleteMeetupById(id);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, deletedMeetupDTO, null);
    }
}
