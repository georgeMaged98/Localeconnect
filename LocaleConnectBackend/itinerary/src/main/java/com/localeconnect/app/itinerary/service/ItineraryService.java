package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.dto.ItineraryDTO;
import com.localeconnect.app.itinerary.dto.ReviewDTO;
import com.localeconnect.app.itinerary.mapper.ItineraryMapper;
import com.localeconnect.app.itinerary.mapper.ReviewMapper;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.model.Review;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import com.localeconnect.app.itinerary.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ReviewRepository reviewRepository;
    private final WebClient webClient;
    private final ItineraryMapper itineraryMapper;
    private final ReviewMapper reviewMapper;


    public ItineraryDTO createItinerary(ItineraryDTO itineraryDTO, Long userId) {
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        if (itinerary == null) {
            return null;
        }
        // Make a synchronous request to the userService and save the itinerary if userId matches
        if (this.checkUserId(userId)) {
            itineraryRepository.save(itinerary);
            return itineraryMapper.toDomain(itinerary);

        } else {
            throw new IllegalArgumentException("Register as user to create an itinerary");
        }
    }

    public ItineraryDTO updateItinerary(ItineraryDTO itineraryDTO, Long userId, Long id) {
        Itinerary itinerary = itineraryMapper.toEntity(itineraryDTO);
        if (itinerary == null) {
            return null;
        }
        itinerary.setId(id);
        if (this.checkUserId(userId)) {

            itineraryRepository.save(itinerary);
            return itineraryMapper.toDomain(itinerary);

        } else {
            throw new IllegalArgumentException("Only registered users can edit their itinerary");
        }
    }

    public void deleteItinerary(Long id, Long userId) {
        if (this.checkUserId(userId)) {
            Optional<Itinerary> optional = itineraryRepository.findById(id);
            optional.ifPresent(itineraryRepository::delete);
        } else {
            throw new IllegalArgumentException("Only registered users can delete their itinerary");
        }
    }

    public List<ItineraryDTO> getAllItineraries() {
        List<Itinerary> itineraries = itineraryRepository.findAll();

        return itineraries.stream().map(
                        itineraryMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<ItineraryDTO> getAllItinerariesByUser(Long userId) {

        List<Itinerary> itineraries = itineraryRepository.findByUserId(userId);
        return itineraries.stream().map(itineraryMapper::toDomain).collect(Collectors.toList());
    }


    public ItineraryDTO getItineraryById(Long id) {
        Optional<Itinerary> optional = itineraryRepository.findById(id);
        return optional.map(itineraryMapper::toDomain).orElse(null);

    }

    //TODO: combine the user information and the review in the frontend
    public ReviewDTO createReview(ReviewDTO reviewDto, Long userId) {
        Review review = reviewMapper.toEntity(reviewDto);
        if (review == null) {
            return null;
        }

        if (!this.checkUserId(reviewDto.getUserId())) {
            throw new IllegalArgumentException("Only registered users can create a review");
        }
        review.setUserId(userId);
        review.setTimestamp(LocalDateTime.now());
        review = reviewRepository.save(review);
        return reviewMapper.toDomain(review);

    }

    public ReviewDTO updateReview(ReviewDTO reviewDTO, Long id) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isEmpty()) {
            throw new EntityNotFoundException("Review not found with id: " + id);
        }

        if (!this.checkUserId(reviewDTO.getUserId())) {
            throw new IllegalArgumentException("Only registered users can edit their reviews");
        }

        if (!existingReview.get().getUserId().equals(reviewDTO.getUserId())) {
            throw new IllegalArgumentException("Users can only edit their own reviews");
        }
        if (!existingReview.get().getItineraryId().equals(reviewDTO.getItineraryId())) {
            throw new IllegalArgumentException("Review does not belong to the specified itinerary");
        }

        Review reviewToUpdate = reviewMapper.toEntity(reviewDTO);
        reviewToUpdate.setId(id);
        reviewToUpdate.setTimestamp(LocalDateTime.now());
        Review updatedReview = reviewRepository.save(reviewToUpdate);
        return reviewMapper.toDomain(updatedReview);
    }


    public void deleteReview(Long id) {
        Optional<Review> existingReview = reviewRepository.findById(id);
        if (existingReview.isEmpty()) {
            throw new EntityNotFoundException("Review not found with id: " + id);
        }

        if (!this.checkUserId(existingReview.get().getUserId())) {
            throw new IllegalArgumentException("Only registered users can delete their reviews");
        }

                reviewRepository.delete(existingReview.get());

    }

    public List<ReviewDTO> getAllReviewsForItinerary(Long itineraryId) {
        List<Review> reviews = reviewRepository.findByItineraryId(itineraryId);

        return reviews.stream().map(
                        reviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    // TODO: returns true for now, needs the user microservice to work properly
    private Boolean checkUserId(Long userId) {
  /* Boolean check = this.webClient.get()
                .uri("http://localhost:8080/api/user/verifyUser/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).onErrorReturn(false).block();
        return Boolean.TRUE.equals(check);

   */
        return true;
    }
}

