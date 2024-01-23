package com.localeconnect.app.itinerary.controller;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.ItineraryShareDTO;
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
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/itinerary")
@AllArgsConstructor
public class ItineraryController {
private final ItineraryService itineraryService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ItineraryDTO> createItinerary(@RequestBody @Valid ItineraryDTO itineraryDTO, @RequestParam(value = "user") Long userId) {
        try {
            ItineraryDTO itinerary = itineraryService.createItinerary(itineraryDTO, userId);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<ItineraryDTO> updateItinerary(@PathVariable("id") Long id, @RequestBody @Valid ItineraryDTO itineraryDTO,@RequestParam(value = "user") Long userId) {

        try {
            ItineraryDTO itinerary = itineraryService.updateItinerary(itineraryDTO,userId,id);
            return ResponseEntity.ok(itinerary);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteItinerary(@PathVariable("id") Long id,@RequestParam(value = "user") Long userId) {
        itineraryService.deleteItinerary(id,userId);
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
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/allByUser")
    public ResponseEntity<List<ItineraryDTO>> getUserItineraries(@RequestParam(value = "user") Long userId) {
        try {
            List<ItineraryDTO> itineraries = itineraryService.getAllItinerariesByUser(userId);
            return ResponseEntity.ok(itineraries);
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }   }
    @PostMapping("/share/{itineraryId}")
    public Mono<ResponseEntity<ItineraryShareDTO>> shareItinerary(@PathVariable("itineraryId") Long itineraryId) {
        return itineraryService.shareItinerary(itineraryId)
                .map(sharedItinerary -> ResponseEntity.ok().body(sharedItinerary))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
