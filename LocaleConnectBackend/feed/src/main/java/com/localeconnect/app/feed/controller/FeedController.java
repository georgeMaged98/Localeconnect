package com.localeconnect.app.feed.controller;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.localeconnect.app.feed.dto.*;
import com.localeconnect.app.feed.response_handler.ResponseHandler;
import com.localeconnect.app.feed.service.FeedService;
import com.localeconnect.app.feed.type.PostType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/create")
    public ResponseEntity<Object> createRegularPost(@RequestBody @Valid RegularPostDTO regularPost) {
        log.info("************entred CREATE POST FEED CONTROLLER**************");
        PostDTO newPostDTO = feedService.createPost(regularPost);

        return ResponseHandler.generateResponse("Success!", HttpStatus.CREATED, newPostDTO, null);
    }

    @DeleteMapping(path = "/delete/{postId}")
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
    public String shareTrip(@RequestBody @Valid TripDTO tripToShare,
                            @RequestParam(value = "authorId") @Valid Long authorId) {
        feedService.shareTrip(tripToShare, authorId);

        return "Successfully shared to feed!";
    }

    @PostMapping("/share-itinerary")
    public String shareItinerary(@RequestBody @Valid ItineraryDTO itineraryToShare,
                                 @RequestParam(value = "authorId") @Valid Long authorId) {
        log.info("************entred SHARE ITI FEEDCONTROLLER **************");
        feedService.shareItinerary(itineraryToShare, authorId);
        log.info("************saved ITI POST IN REPO**************");
        return "Successfully shared to feed!";
    }

    @PostMapping("/share-meetup")
    public String shareMeetup(@RequestBody @Valid MeetupDTO meetupToShare,
                              @RequestParam(value = "authorId") @Valid Long authorId) {
        feedService.shareMeetup(meetupToShare, authorId);

        return "Successfully shared to feed!";
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<Object> getPostById(@PathVariable("postId") Long postId) {
        PostDTO foundPost = feedService.getPostById(postId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, foundPost, null);
    }
    @GetMapping("/{postId}/like-list")
    public ResponseEntity<Object> getPostLikes(@PathVariable("postId") Long postId) {
        List<String> usersLiked = feedService.getPostLikes(postId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, usersLiked, null);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Object> likePost(@PathVariable("postId") Long postId,
                                           @RequestBody @Valid LikeDTO likeDTO) {
        PostDTO postLiked = feedService.likePost(postId, likeDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, postLiked, null);
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<Object> unlikePost(@PathVariable("postId") Long postId,
                                             @RequestBody @Valid LikeDTO likeDTO) {
        PostDTO postUnliked = feedService.unlikePost(postId, likeDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, postUnliked, null);
    }
    @GetMapping( "/author/{authorId}")
    public ResponseEntity<Object> getPostsByAuthorId(@PathVariable("authorId") Long authorId) {
        List<PostDTO> foundPost = feedService.getPostsByAuthorId(authorId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, foundPost, null);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchPost(@RequestParam("keyword") String keyword) {
        List<PostDTO> searchedPosts = feedService.searchPosts(keyword);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, searchedPosts, null);
     }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterPost(@RequestParam(value = "postType", required = false) @Valid String postTypeString) {
        PostType postType = PostType.valueOf(postTypeString.toUpperCase());

        List<PostDTO> filteredPosts = feedService.filterPosts(postType);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, filteredPosts, null);
    }
    @GetMapping("/home/{userId}")
    public ResponseEntity<Object> getUserFeed(@PathVariable("userId") Long userId) {
        List<PostDTO> feed =  feedService.generateUserFeed(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, feed, null);
    }
    @GetMapping("/{postId}/like-count")
    public ResponseEntity<Object> getPostLikeCount(@PathVariable("postId") Long postId) {
        int count = feedService.getPostLikes(postId).size();
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, count, null);
    }

}
