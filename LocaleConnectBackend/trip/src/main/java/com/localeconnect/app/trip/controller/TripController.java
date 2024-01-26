package com.localeconnect.app.trip.controller;

import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.response_handler.ResponseHandler;
import com.localeconnect.app.trip.service.TripService;
import jakarta.validation.Valid;
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

    @PostMapping("/create")
    public ResponseEntity<Object> createTrip(@RequestBody @Valid TripDTO tripDTO) {
        TripDTO trip = tripService.createTrip(tripDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, trip, null);
    }
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
    @GetMapping(path = "/allByLocalguide/{localguideId}")
    public ResponseEntity<Object> getLocalguideTrips(@PathVariable("localguideId") Long localguideId) {
        List<TripDTO> trips = tripService.getAllTripsByLocalguide(localguideId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, trips, null);
    }

    @PutMapping("/update/{trip_id}")
    public ResponseEntity<Object> updateTrip(@PathVariable ("trip_id") Long tripId,
                                             @RequestBody TripDTO tripDTO) {
        TripDTO trip = tripService.updateTrip(tripId, tripDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, trip,null);
    }
    @DeleteMapping("/delete/{trip_id}")
    public ResponseEntity<Object> deleteTrip(@PathVariable ("trip_id") Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null,null);
    }

}
