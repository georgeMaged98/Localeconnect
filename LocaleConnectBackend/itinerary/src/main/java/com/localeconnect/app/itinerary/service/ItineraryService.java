package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;

    //TODO: configure service communication
    @Autowired
    public ItineraryService(ItineraryRepository itineraryRepository) {
        this.itineraryRepository = itineraryRepository;
    }

    public Itinerary createItinerary(ItineraryDTO itineraryDTO) {
       // TODO: configure validation
        Itinerary itinerary = Itinerary.builder()
                .name(itineraryDTO.getName())
                .description(itineraryDTO.getDescription())
                .numberOfDays(itineraryDTO.getNumberOfDays())
                .dailyActivities(itineraryDTO.getDailyActivities())
                .placesToVisit(itineraryDTO.getPlacesToVisit())
                .tags(itineraryDTO.getTags())
                .imageUrls(itineraryDTO.getImageUrls()).build();

        return itineraryRepository.save(itinerary);
    }

}

