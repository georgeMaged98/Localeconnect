package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.ReviewDTO;
import com.localeconnect.app.itinerary.exception.ReviewNotFoundException;
import com.localeconnect.app.itinerary.exception.ReviewValidationException;
import com.localeconnect.app.itinerary.exception.UnauthorizedUserException;
import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.Tag;
import com.localeconnect.app.itinerary.exception.ItineraryAlreadyExistsException;
import com.localeconnect.app.itinerary.exception.ItineraryNotFoundException;
import com.localeconnect.app.itinerary.exception.UnauthorizedUserException;
import com.localeconnect.app.itinerary.mapper.ItineraryMapper;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.model.Review;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import com.localeconnect.app.itinerary.repository.ReviewRepository;
import com.localeconnect.app.itinerary.repository.ItinerarySpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.time.LocalDateTime;
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
    }

    public ItineraryDTO updateItinerary(ItineraryDTO itineraryDTO, Long id) {
        Itinerary itinerary = mapper.toEntity(itineraryDTO);
        if(itinerary == null){
            return null;
        }
        itinerary.setId(id);
        if (this.checkUserId(userId)) {

            itineraryRepository.save(itinerary);
        return mapper.toDomain(itinerary);
    }

    public void deleteItinerary(Long id) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new ItineraryNotFoundException("Itinerary not found for id: " + id));

        if (!this.checkUserId(itinerary.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can delete their itinerary");
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
        return itineraries != null ? itineraries.stream().map(mapper::toDomain).collect(Collectors.toList()) : new ArrayList<>();
    }


    public ItineraryDTO getItineraryById(Long id) {
        Optional<Itinerary> optional = itineraryRepository.findById(id);
        return optional.map(itineraryMapper::toDomain).orElse(null);

    }

    //TODO: combine the user information and the review in the frontend
    public ReviewDTO createReview(ReviewDTO reviewDto, Long userId) {
        Review review = reviewMapper.toEntity(reviewDto);
        if (review == null) {
            throw new ReviewValidationException("Review data is invalid");
        }

        if (!this.checkUserId(reviewDto.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can create a review");
        }
        review.setUserId(userId);
        review.setTimestamp(LocalDateTime.now());
        review = reviewRepository.save(review);
        return reviewMapper.toDomain(review);
    }


    public ReviewDTO updateReview(ReviewDTO reviewDTO, Long id) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        if (!this.checkUserId(reviewDTO.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can edit their reviews");
        }

        if (!existingReview.getUserId().equals(reviewDTO.getUserId())) {
            throw new UnauthorizedUserException("Users can only edit their own reviews");
        }

        Review reviewToUpdate = reviewMapper.toEntity(reviewDTO);
        reviewToUpdate.setId(id);
        reviewToUpdate.setTimestamp(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(reviewToUpdate);
        return reviewMapper.toDomain(updatedReview);
    }
        return optional.map(mapper::toDomain).orElseThrow(() -> new ItineraryNotFoundException("Itinerary not found"));

    }

    public List<ItineraryDTO> searchByName(String name) {
        if (name == null) {
            return null;
        }
        List<Itinerary> itineraries = itineraryRepository.findAllIByNameIgnoreCaseLike(name);
        return itineraries.stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    public List<ItineraryDTO> filter(String place, Tag tag, Integer days) {
        if (place == null && tag == null && days < 1) {
            return null;
        }
        Specification<Itinerary> spec = Specification.where(ItinerarySpecification.hasPlace(place))
                .and(ItinerarySpecification.hasTag(tag)).and(ItinerarySpecification.maxNumberOfDays(days));
        List<Itinerary> itineraries = itineraryRepository.findAll(spec);
        return itineraries.stream().map(mapper::toDomain).collect(Collectors.toList());

    }

    // TODO. returns true for now, needs the user microservice to work properly
    private Boolean checkUserId(Long userId){
    /*   Boolean check = this.webClient.get()
                .uri("http://localhost:8080/api/user/verifyUser/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block();
        return Boolean.TRUE.equals(check);

     */
        return true;
    }
}

