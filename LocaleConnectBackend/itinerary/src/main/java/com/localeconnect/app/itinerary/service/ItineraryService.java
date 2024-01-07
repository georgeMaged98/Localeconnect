package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final WebClient webClient;

    public Itinerary createItinerary( @Valid ItineraryDTO itineraryDTO, String userId) {

        // Make a synchronous request to the userService and save the itinerary if userId matches
        Boolean check = this.webClient.get()
                .uri("http://localhost:8080/api/user/verifyUser/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block();
        if (Boolean.TRUE.equals(check)) {
            Itinerary itinerary = Itinerary.builder()
                    .name(itineraryDTO.getName())
                    .description(itineraryDTO.getDescription())
                    .numberOfDays(itineraryDTO.getNumberOfDays())
                    .dailyActivities(itineraryDTO.getDailyActivities())
                    .placesToVisit(itineraryDTO.getPlacesToVisit())
                    .tags(itineraryDTO.getTags())
                    .imageUrls(itineraryDTO.getImageUrls()).build();
            return itineraryRepository.save(itinerary);

        } else {
            throw new IllegalArgumentException("Register as user to create an itinerary");
        }
    }
}

