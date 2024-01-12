package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.mapper.ItineraryMapper;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final WebClient webClient;
    private final ItineraryMapper mapper;


    public ItineraryDTO createItinerary(ItineraryDTO itineraryDTO, Long userId) {
        Itinerary itinerary = mapper.toEntity(itineraryDTO);
        if(itinerary == null){
            return null;
        }
        // Make a synchronous request to the userService and save the itinerary if userId matches
        if (this.checkUserId(userId)) {
            itineraryRepository.save(itinerary);
            return mapper.toDomain(itinerary);

        } else {
            throw new IllegalArgumentException("Register as user to create an itinerary");
        }
    }

    public ItineraryDTO updateItinerary(ItineraryDTO itineraryDTO, Long userId, Long id) {
        Itinerary itinerary = mapper.toEntity(itineraryDTO);
        if(itinerary == null){
            return null;
        }
        itinerary.setId(id);
        if (this.checkUserId(userId)) {

            itineraryRepository.save(itinerary);
            return mapper.toDomain(itinerary);

        } else {
            throw new IllegalArgumentException("Only registered users can edit their itinerary");
        }
    }

    public void deleteItinerary(Long id, Long userId) {
        if(this.checkUserId(userId)){
            Optional<Itinerary> optional = itineraryRepository.findById(id);
            optional.ifPresent(itineraryRepository::delete);
        }
         else {
            throw new IllegalArgumentException("Only registered users can delete their itinerary");
        }
    }

    public List<ItineraryDTO> getAllItineraries() {
        List<Itinerary> itineraries = itineraryRepository.findAll();

        return itineraries.stream().map(
                        mapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ItineraryDTO> getAllItinerariesByUser(Long userId) {

        List<Itinerary> itineraries = itineraryRepository.findByUserId(userId);
        return itineraries.stream().map(mapper::toDomain).collect(Collectors.toList());
    }


    public ItineraryDTO getItineraryById(Long id) {
        Optional<Itinerary> optional = itineraryRepository.findById(id);
        return optional.map(mapper::toDomain).orElse(null);

    }

    // TODO. returns true for now, needs the user microservice to work properly
    private Boolean checkUserId(Long userId) {
  /* Boolean check = this.webClient.get()
                .uri("http://localhost:8080/api/user/verifyUser/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).onErrorReturn(false).block();
        return Boolean.TRUE.equals(check);

   */

        return true;
    }
}

