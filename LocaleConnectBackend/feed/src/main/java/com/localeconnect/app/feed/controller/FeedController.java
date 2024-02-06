package com.localeconnect.app.feed.controller;
import com.localeconnect.app.feed.dto.*;
import com.localeconnect.app.feed.response_handler.ResponseHandler;
import com.localeconnect.app.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/")
    public ResponseEntity<Object> createRegularPost(@RequestBody @Valid RegularPostDTO regularPost) {
        PostDTO newPostDTO = feedService.createPost(regularPost);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, newPostDTO, null);
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Object> deletePostById(@PathVariable("postId") Long postId) {
        PostDTO deletedPostDTO = feedService.deletePost(postId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, deletedPostDTO, null);
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable("postId") Long postId, @RequestBody @Valid CommentDTO commentDTO) {
        PostDTO updatedPost = feedService.addComment(postId, commentDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, updatedPost, null);
    }

    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {
        PostDTO updatedPost = feedService.deleteComment(postId, commentId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, updatedPost, null);
    }

    @PostMapping("/share-trip")
    public ResponseEntity<Object> shareTrip(@RequestBody @Valid TripDTO tripToShare,
                                            @RequestParam @Valid Long authorId) {
        PostDTO tripPost = feedService.shareTrip(tripToShare, authorId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, tripPost, null);
    }

    @PostMapping("/share-itinerary")
    public ResponseEntity<Object> shareItinerary(@RequestBody @Valid ItineraryDTO itineraryToShare,
                                                 @RequestParam @Valid Long authorId) {
        PostDTO itineraryPost = feedService.shareItinerary(itineraryToShare, authorId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, itineraryPost, null);
    }

    @PostMapping("/share-meetup")
    public ResponseEntity<Object> shareMeetup(@RequestBody @Valid MeetupDTO meetupToShare,
                                              @RequestParam @Valid Long authorId) {
        PostDTO meetupPost = feedService.shareMeetup(meetupToShare, authorId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, meetupPost, null);
    }
}
