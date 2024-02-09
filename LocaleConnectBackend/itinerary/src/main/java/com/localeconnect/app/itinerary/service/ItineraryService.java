package com.localeconnect.app.itinerary.service;

import com.localeconnect.app.itinerary.dto.*;
import com.localeconnect.app.itinerary.exception.*;
import com.localeconnect.app.itinerary.mapper.ItineraryMapper;
import com.localeconnect.app.itinerary.mapper.ReviewMapper;
import com.localeconnect.app.itinerary.model.Itinerary;
import com.localeconnect.app.itinerary.model.Review;
import com.localeconnect.app.itinerary.repository.ItineraryRepository;
import com.localeconnect.app.itinerary.repository.ItinerarySpecification;
import com.localeconnect.app.itinerary.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
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
public class ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final WebClient webClient;
    private final ItineraryMapper mapper;
    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;


    public ItineraryDTO createItinerary(ItineraryDTO itineraryDTO, Long userId) {
        Itinerary itinerary = mapper.toEntity(itineraryDTO);
        if (itinerary == null) {
            throw new ItineraryNotFoundException("Itinerary data is invalid");
        }

        if (!this.checkUserId(userId)) {
            throw new UnauthorizedUserException("Register as user to create an itinerary");
        }

        if (this.itineraryRepository.existsByUserIdAndName(userId, itineraryDTO.getName())) {
            throw new ItineraryAlreadyExistsException("This user already created this itinerary.");
        }
        List<String> images = itineraryDTO.getImageUrls();

        if (!images.isEmpty()) {
            // Save image in GCP
            GCPResponseDTO gcpResponse = saveImageToGCP(itineraryDTO.getImageUrls().get(0));
            String imageUrl = gcpResponse.getData();

            itinerary.setImageUrls(List.of(imageUrl));
        }else{
            itinerary.setImageUrls(new ArrayList<>());
        }
        itinerary.setUserId(userId);
        itineraryRepository.save(itinerary);
        return mapper.toDomain(itinerary);
    }

    public ItineraryDTO updateItinerary(ItineraryDTO itineraryDTO, Long id) {
        Itinerary itinerary = mapper.toEntity(itineraryDTO);
        if (itinerary == null) {
            throw new ItineraryNotFoundException("Itinerary data is invalid");
        }

        itinerary.setId(id);

        if (!this.checkUserId(itinerary.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can edit their itinerary");
        }

        itineraryRepository.save(itinerary);
        return mapper.toDomain(itinerary);
    }

    public void deleteItinerary(Long id) {
        Itinerary itinerary = itineraryRepository.findById(id)
                .orElseThrow(() -> new ItineraryNotFoundException("Itinerary not found for id: " + id));

        if (!this.checkUserId(itinerary.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can delete their itinerary");
        }

        itineraryRepository.delete(itinerary);
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
        return optional.map(mapper::toDomain).orElseThrow(() -> new ItineraryNotFoundException("Itinerary not found"));

    }


    //TODO: combine the user information and the review in the frontend
    public ReviewDTO createReview(ReviewDTO reviewDto, Long userId, Long itineraryId) {
        Review review = reviewMapper.toEntity(reviewDto);
        if (review == null) {
            throw new ReviewValidationException("Review data is invalid");
        }

        if (!this.checkUserId(reviewDto.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can create a review");
        }

        if (this.itineraryRepository.findById(itineraryId).isEmpty()) {
            throw new ItineraryNotFoundException("could not find itinerary for this review");
        }
        review.setItineraryId(itineraryId);
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

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with id: " + id));

        if (!this.checkUserId(review.getUserId())) {
            throw new UnauthorizedUserException("Only registered users can delete their reviews");
        }

        reviewRepository.delete(review);
    }

    public List<ReviewDTO> getAllReviewsForItinerary(Long itineraryId) {
        List<Review> reviews = reviewRepository.findByItineraryId(itineraryId);

        return reviews.stream().map(
                        reviewMapper::toDomain)
                .collect(Collectors.toList());
    }


    // TODO: add a shareItinerary method in the feed
    public Mono<ItineraryShareDTO> shareItinerary(Long itineraryId) {
        return Mono.just(itineraryRepository.findById(itineraryId))
                .map(itinerary -> {
                    ItineraryShareDTO shareDTO
                            = new ItineraryShareDTO();
                    if (itinerary.isPresent()) {
                        shareDTO.setId(itinerary.get().getId());
                        shareDTO.setName(itinerary.get().getName());
                        shareDTO.setDescription(itinerary.get().getDescription());
                    }
                    return shareDTO;
                })
                .flatMap(this::postToFeed)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Itinerary not found with id: " + itineraryId)));
    }


    private Boolean checkUserId(Long userId) {
        Boolean check = this.webClient.get()
                .uri("http://user-service:8084/api/user/auth/exists/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block();
        return check != null && check;
    }


    private Mono<ItineraryShareDTO> postToFeed(ItineraryShareDTO itineraryShareDTO) {
        return webClient.post()
                .uri("http://feed-service:8081/api/feed/share-itinerary")
                .bodyValue(itineraryShareDTO)
                .retrieve()
                .bodyToMono(ItineraryShareDTO.class);
    }

    private GCPResponseDTO saveImageToGCP(String image) {
        ResponseEntity<GCPResponseDTO> responseEntity = webClient.post()
                .uri("http://gcp-service:5005/api/gcp/?filename=itinerary")
                .bodyValue(image)
                .retrieve()
                .toEntity(GCPResponseDTO.class)
                .block();
        return responseEntity.getBody();
    }
}

