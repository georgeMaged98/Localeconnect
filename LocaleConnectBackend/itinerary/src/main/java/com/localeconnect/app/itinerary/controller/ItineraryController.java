package com.localeconnect.app.itinerary.controller;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItineraryDTO> createItinerary(@RequestBody @Valid ItineraryDTO itineraryDTO) {
        try {
            ItineraryDTO itinerary = itineraryService.createItinerary(itineraryDTO, itineraryDTO.getUserId());
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ItineraryDTO> updateItinerary(@PathVariable("id") Long id, @RequestBody @Valid ItineraryDTO itineraryDTO) {

        try {
            ItineraryDTO itinerary = itineraryService.updateItinerary(itineraryDTO, id);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteItinerary(@PathVariable("id") Long id) {
        itineraryService.deleteItinerary(id);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable("id") Long id) {
        try {
            ItineraryDTO itinerary = itineraryService.getItineraryById(id);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }    }


    @GetMapping(path = "/all")
    public ResponseEntity<List<ItineraryDTO>> getAllItineraries() {
        try {
            List<ItineraryDTO> itineraries = itineraryService.getAllItineraries();
            return ResponseEntity.ok(itineraries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(path = "/allByUser/{user}")
    public ResponseEntity<List<ItineraryDTO>> getUserItineraries(@PathVariable("user") Long userId) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.getAllItinerariesByUser(userId);
            return ResponseEntity.ok(itineraries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<ItineraryDTO>> searchItineraries(@RequestParam("name") String name) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.searchByName(name);
            return ResponseEntity.ok(itineraries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ItineraryDTO>> searchItineraries(
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "tag", required = false) String tag
    ) {

        List<ItineraryDTO> itineraries = itineraryService.filter(place, tag);
        return ResponseEntity.ok(itineraries);
    }

}
