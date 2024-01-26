package com.localeconnect.app.trip.controller;

import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.model.Trip;
import com.localeconnect.app.trip.response_handler.ResponseHandler;
import com.localeconnect.app.trip.service.TripService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin
@Slf4j
@RequestMapping("/api/trip")
public class TripController {
    private final TripService tripService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllTrips() {
        List<TripDTO> trips = tripService.getAllTrips();
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, trips, null);
    }

    @GetMapping("/{trip_id}")
    public ResponseEntity<Object> getTripById(@PathVariable ("trip_id") Long tripId) {
        TripDTO trip = tripService.getById(tripId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, trip, null);
    }
}
