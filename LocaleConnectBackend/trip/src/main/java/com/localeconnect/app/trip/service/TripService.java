package com.localeconnect.app.trip.service;

import com.localeconnect.app.trip.dto.NotificationDTO;
import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.dto.TripReviewDTO;
import com.localeconnect.app.trip.exceptions.ResourceNotFoundException;
import com.localeconnect.app.trip.exceptions.ValidationException;
import com.localeconnect.app.trip.exceptions.LogicException;
import com.localeconnect.app.trip.mapper.TripMapper;
import com.localeconnect.app.trip.mapper.TripReviewMapper;
import com.localeconnect.app.trip.model.Trip;
import com.localeconnect.app.trip.model.TripReview;
import com.localeconnect.app.trip.repository.TripRepository;
import com.localeconnect.app.trip.repository.TripReviewRepository;
import com.localeconnect.app.trip.repository.TripSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final TripReviewMapper tripReviewMapper;
    private final WebClient webClient;
    private final TripReviewRepository tripReviewRepository;

    public TripDTO createTrip(TripDTO tripDTO) {
        //check if creator (localguide) exists
        if(!this.checkUserId(tripDTO.getLocalguideId()))
            throw new ValidationException("Register as a Localguide to create a Trip");

        //check if this Trip already exists
        Optional<Trip> optionalTrip = tripRepository.findByLocalguideIdAndName(tripDTO.getLocalguideId(), tripDTO.getName());
        if (optionalTrip.isEmpty())
            throw new LogicException("A Trip with this name already exists");

        tripRepository.save(tripMapper.toEntity(tripDTO));
        return tripDTO;
    }

    public List<TripDTO> getAllTrips() {
       return tripRepository.findAll().stream()
               .map(tripMapper::toDomain)
               .collect(Collectors.toList());
    }

    public TripDTO getById(Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        return tripMapper.toDomain(optionalTrip.get());
    }

    public List<TripDTO> getAllTripsByLocalguide(Long localguideId) {

        Optional<List<Trip>> trips = tripRepository.findByLocalguideId(localguideId);
        return trips.map(tripList -> tripList.stream().map(tripMapper::toDomain).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    public TripDTO updateTrip( Long tripId, TripDTO tripDTO) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        //update the trip
        Trip tripToUpdate = optionalTrip.get();
        tripMapper.updateTripFromDto(tripDTO, tripToUpdate);
        //send notifications to all travelers
        List<Long> travelers = tripToUpdate.getTravelers();
        for (Long traveler : travelers) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + tripId +" Got Updated!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiver(traveler);
            newNotification.setSender(tripToUpdate.getLocalguideId());
        }
        tripRepository.save(tripToUpdate);
        return tripMapper.toDomain(optionalTrip.get());
    }

    public void deleteTrip(Long tripId) {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);

        if (optionalTrip.isEmpty())
            throw new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!");
        //update the trip
        Trip tripToDelete = optionalTrip.get();
        //send notifications to all travelers
        List<Long> travelers = tripToDelete.getTravelers();
        for (Long traveler : travelers) {
            NotificationDTO newNotification = new NotificationDTO();
            newNotification.setTitle("New Notification");
            newNotification.setMessage("Meetup " + tripId +" Got Deleted!");
            newNotification.setSentAt(LocalDateTime.now());
            newNotification.setReceiver(traveler);
            newNotification.setSender(tripToDelete.getLocalguideId());
        }
        tripRepository.delete(tripToDelete);
    }

    public TripDTO searchTrip(String tripName) {
        Optional<Trip> trip = tripRepository.findByName(tripName);

        if(trip.isEmpty())
            throw new ResourceNotFoundException("A trip with the name " + tripName + " does not exist!");

        return tripMapper.toDomain(trip.get());
    }

    public List<TripDTO> filter(String destination, Double traveltime, List<String> languages) {
        if (destination == null && traveltime <= 0 && languages == null)
            return null;

        Specification<Trip> specif = Specification.where(TripSpecification.getDestination(destination))
                .and(TripSpecification.maxDuration(traveltime)).and(TripSpecification.hasLanguages(languages));
        List<Trip> trips = tripRepository.findAll(specif);
        return trips.stream().map(tripMapper::toDomain).collect(Collectors.toList());
    }

    public TripReviewDTO createReview(TripReviewDTO tripReviewDTO, Long userId, Long tripId) {
        if(!checkUserId(userId))
            throw new ValidationException("Register to create a Review");
        if(tripId == null || !tripRepository.existsById(tripId))
            throw new ResourceNotFoundException("Trip with this ID does not exist");
        TripReview tripReview = tripReviewMapper.toEntity(tripReviewDTO);
        tripReview.setTripId(tripId);
        tripReview.setUserId(userId);
        tripReview.setTimestamp(LocalDateTime.now());
        tripReview = tripReviewRepository.save(tripReview);
        return tripReviewMapper.toDomain(tripReview);
    }

    private Boolean checkUserId(Long userId) {
        Boolean check = this.webClient.get()
                .uri("http://user-service/api/user/exists/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block();
        return check != null && check;
    }

}
