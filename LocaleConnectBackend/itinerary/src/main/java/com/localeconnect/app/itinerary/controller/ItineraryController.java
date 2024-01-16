package com.localeconnect.app.itinerary.controller;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.Tag;
import com.localeconnect.app.itinerary.exception.ItineraryAlreadyExistsException;
import com.localeconnect.app.itinerary.exception.ItineraryNotFoundException;
import com.localeconnect.app.itinerary.exception.UnauthorizedUserException;
import com.localeconnect.app.itinerary.service.ItineraryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
@AllArgsConstructor
public class ItineraryController {
private final ItineraryService itineraryService;

    @PostMapping("/create")
    public ResponseEntity<?> createItinerary(@RequestBody @Valid ItineraryDTO itineraryDTO) {
        try {
            ItineraryDTO itinerary = itineraryService.createItinerary(itineraryDTO, itineraryDTO.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(itinerary);
        } catch (UnauthorizedUserException | ItineraryAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updateItinerary(@PathVariable("id") Long id, @RequestBody @Valid ItineraryDTO itineraryDTO) {
        try {
            ItineraryDTO updatedItinerary = itineraryService.updateItinerary(itineraryDTO, id);
            return ResponseEntity.ok(updatedItinerary);
        } catch (UnauthorizedUserException | ItineraryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable("id") Long id) {
        try {
            itineraryService.deleteItinerary(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UnauthorizedUserException | ItineraryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getItineraryById(@PathVariable("id") Long id) {
        try {
            ItineraryDTO itinerary = itineraryService.getItineraryById(id);
            return ResponseEntity.ok(itinerary);
        } catch (ItineraryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllItineraries() {
        try {
            List<ItineraryDTO> itineraries = itineraryService.getAllItineraries();
            return ResponseEntity.ok(itineraries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/allByUser/{user}")
    public ResponseEntity<?> getUserItineraries(@PathVariable("user") Long userId) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.getAllItinerariesByUser(userId);
            return ResponseEntity.ok(itineraries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> searchItineraries(@RequestParam("name") String name) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.searchByName(name);
            return ResponseEntity.ok(itineraries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterItineraries(
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "tag", required = false) Tag tag,
            @RequestParam(value = "days", required = false) Integer days) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.filter(place, tag, days);
            return ResponseEntity.ok(itineraries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
