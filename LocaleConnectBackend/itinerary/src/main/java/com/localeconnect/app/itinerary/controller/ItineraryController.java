package com.localeconnect.app.itinerary.controller;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.ReviewDTO;
import com.localeconnect.app.itinerary.exception.ReviewNotFoundException;
import com.localeconnect.app.itinerary.exception.ReviewValidationException;
import com.localeconnect.app.itinerary.exception.UnauthorizedUserException;
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
            ItineraryDTO updatedItinerary = itineraryService.updateItinerary(itineraryDTO, id);
            return ResponseEntity.ok(updatedItinerary);
        } catch (UnauthorizedUserException | ItineraryNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteItinerary(@PathVariable("id") Long id,@RequestParam(value = "user") Long userId) {
        itineraryService.deleteItinerary(id,userId);
          }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ItineraryDTO> getItineraryById(@PathVariable("id") Long id) {
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
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/create-review")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewDTO reviewDto) {
        try {
            ReviewDTO review = itineraryService.createReview(reviewDto, reviewDto.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
        } catch (UnauthorizedUserException | ReviewValidationException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(path = "/update-review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable("id") Long id, @RequestBody @Valid ReviewDTO reviewDTO) {
        try {
            ReviewDTO review = itineraryService.updateReview(reviewDTO, id);
            return ResponseEntity.ok(review);
        } catch (UnauthorizedUserException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/delete-review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) {
        try {
            itineraryService.deleteReview(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UnauthorizedUserException | ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping(path = "/all-reviews/{itineraryId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(@PathVariable("itineraryId") Long itineraryId) {
        try {
            List<ReviewDTO> reviews = itineraryService.getAllReviewsForItinerary(itineraryId);
            return ResponseEntity.ok(reviews);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
