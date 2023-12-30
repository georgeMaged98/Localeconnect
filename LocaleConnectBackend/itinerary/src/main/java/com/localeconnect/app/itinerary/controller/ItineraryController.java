package com.localeconnect.app.itinerary.controller;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itinerary")
@RequiredArgsConstructor
public class ItineraryController {
private final ItineraryService itineraryService;
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Itinerary createItinerary(@RequestBody ItineraryDTO itineraryDTO){
     return itineraryService.createItinerary(itineraryDTO);
    }

}
