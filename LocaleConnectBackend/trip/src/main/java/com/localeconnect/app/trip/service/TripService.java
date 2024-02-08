package com.localeconnect.app.trip.service;

import com.localeconnect.app.trip.dto.NotificationDTO;
import com.localeconnect.app.trip.dto.TripDTO;
import com.localeconnect.app.trip.dto.TripReviewDTO;
import com.localeconnect.app.trip.dto.TripShareDTO;
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
import reactor.core.publisher.Mono;


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
        if (tripRepository.findByLocalguideIdAndName(tripDTO.getLocalguideId(), tripDTO.getName()).isPresent())
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
        Trip optionalTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!"));

        return tripMapper.toDomain(optionalTrip);
    }

    public List<TripDTO> getAllTripsByLocalguide(Long localguideId) {

        Optional<List<Trip>> trips = tripRepository.findByLocalguideId(localguideId);
        return trips.map(tripList -> tripList.stream().map(tripMapper::toDomain).collect(Collectors.toList())).orElseGet(ArrayList::new);
    }

    public TripDTO updateTrip( Long tripId, TripDTO tripDTO) {
        Trip tripToUpdate = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!"));

        tripToUpdate.setId(tripId);
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
        return tripMapper.toDomain(tripToUpdate);
    }

    public void deleteTrip(Long tripId) {
        Trip tripToDelete = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("A trip with the id " + tripId + " does not exist!"));

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
        Trip trip = tripRepository.findByName(tripName)
                .orElseThrow(() -> new ResourceNotFoundException("A trip with the name " + tripName + " does not exist!"));

        return tripMapper.toDomain(trip);
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
        if(!this.checkUserId(userId))
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

    public TripReviewDTO updateReview(TripReviewDTO tripReviewDTO, Long id) {
        TripReview existingTripReview = tripReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (!this.checkUserId(tripReviewDTO.getUserId())) {
            throw new ValidationException("Only registered users can edit their reviews");
        }

        if (!existingTripReview.getUserId().equals(tripReviewDTO.getUserId())) {
            throw new ValidationException("Users can only edit their own reviews");
        }

        TripReview reviewToUpdate = tripReviewMapper.toEntity(tripReviewDTO);
        reviewToUpdate.setTripReviewId(id);
        reviewToUpdate.setTimestamp(LocalDateTime.now());
        tripReviewMapper.updateTripReviewFromDto(tripReviewDTO, reviewToUpdate);
        TripReview updatedReview = tripReviewRepository.save(reviewToUpdate);
        return tripReviewMapper.toDomain(updatedReview);
    }

    public void deleteReview(Long id) {
        TripReview review = tripReviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (!this.checkUserId(review.getUserId())) {
            throw new ValidationException("Only registered users can delete their reviews");
        }

        tripReviewRepository.delete(review);
    }

    public List<TripReviewDTO> getAllReviewsForTrip(Long tripId) {
        List<TripReview> reviews = tripReviewRepository.findByTripId(tripId);

        return reviews.stream().map(
                        tripReviewMapper::toDomain)
                .collect(Collectors.toList());
    }
    // TODO: add shareTrip method in feed
    public Mono<TripShareDTO> shareTrip(Long tripId) {
        return Mono.just(tripRepository.findById(tripId))
                .map(trip -> {
                    TripShareDTO shareDTO
                            = new TripShareDTO();
                    if (trip.isPresent()) {
                        shareDTO.setId(trip.get().getId());
                        shareDTO.setName(trip.get().getName());
                        shareDTO.setDescription(trip.get().getDescription());
                    }
                    return shareDTO;
                })
                .flatMap(this::postToFeed)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Trip not found with id: " + tripId)));
    }
    private Mono<TripShareDTO> postToFeed(TripShareDTO tripShareDTO) {
        return webClient.post()
                .uri("http://feed-service:8081/api/feed/share-trip")
                .bodyValue(tripShareDTO)
                .retrieve()
                .bodyToMono(TripShareDTO.class);
    }
    private Boolean checkUserId(Long userId) {
        Boolean check = this.webClient.get()
                .uri("http://user-service:8084/api/user/exists/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block();
        return check != null && check;
    }

}
