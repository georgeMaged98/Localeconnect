package com.localeconnect.app.feed.service;

import com.localeconnect.app.feed.dto.*;
import com.localeconnect.app.feed.exceptions.LogicException;
import com.localeconnect.app.feed.exceptions.ResourceNotFoundException;
import com.localeconnect.app.feed.mapper.CommentMapper;
import com.localeconnect.app.feed.mapper.PostMapper;
import com.localeconnect.app.feed.model.Comment;
import com.localeconnect.app.feed.model.Post;
import com.localeconnect.app.feed.repository.CommentRepository;
import com.localeconnect.app.feed.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class FeedService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final WebClient webClient;

    public PostDTO createPost(PostDTO postDTO){

        Post post = postMapper.toEntity(postDTO);
        // CHECK USER EXISTS
        long authorId = post.getAuthorID();
        if (!checkUserExists(authorId))
            throw new ResourceNotFoundException("No User Found with id: " + authorId + "!");

        Post createdPost = postRepository.save(post);

        // return saved post
        return postMapper.toDomain(createdPost);
    }

    public PostDTO deletePost(Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();

        postRepository.delete(actualPost);
        // return deleted post
        return postMapper.toDomain(actualPost);
    }

    public PostDTO addComment(Long postId, CommentDTO commentDTO){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();
        Comment comment = commentMapper.toEntity(commentDTO);

        // CHECK USER EXISTS
        long authorId = comment.getAuthorID();
        if (!checkUserExists(authorId))
            throw new ResourceNotFoundException("User with id " + authorId + " does not exist!");

        actualPost.addComment(comment);
        postRepository.save(actualPost);
        // return saved post
        return postMapper.toDomain(actualPost);
    }

    public PostDTO deleteComment(Long postId, Long commentId){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();

        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isEmpty())
            throw new ResourceNotFoundException("No Comment Found with id: " + commentId + "!");

       Comment actualComment = comment.get();
       if (!Objects.equals(actualComment.getPost().getId(), postId))
           throw new LogicException("Comment with id " + commentId + " Does Not Belong To Post with Id: " + postId);

       actualPost.removeComment(actualComment);

        return postMapper.toDomain(actualPost);
    }
    public PostDTO shareTrip(TripDTO tripShareDTO) {
        Post post = new Post();
        post.setAuthorID(tripShareDTO.getLocalguideId());
        post.setPostType("trip");
        post.setContent(createContentFromTrip(tripShareDTO));
        post.setDate(LocalDateTime.now());
        post.setImages(tripShareDTO.getImageUrls());

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }

    public PostDTO shareItinerary(ItineraryDTO itineraryShareDTO) {
        Post post = new Post();
        post.setAuthorID(itineraryShareDTO.getUserId());
        post.setPostType("itinerary");
        post.setContent(createContentFromItinerary(itineraryShareDTO));
        post.setDate(LocalDateTime.now());
        post.setImages(itineraryShareDTO.getImageUrls()); // Assuming Post has a list of image URLs

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }
    public PostDTO shareMeetup(MeetupDTO meetupShareDTO) {
        Post post = new Post();
        post.setAuthorID(meetupShareDTO.getCreatorId());
        post.setPostType("meetup");
        post.setContent(createContentFromMeetup(meetupShareDTO));
        post.setDate(LocalDateTime.now());

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }

    private String createContentFromItinerary(ItineraryDTO dto) {
        return String.format("Itinerary: %s, Days: %d, Places: %s, Description: %s",
                dto.getName(), dto.getNumberOfDays(), String.join(", ", dto.getPlacesToVisit()), dto.getDescription());
    }

    private String createContentFromTrip(TripDTO dto) {
        return String.format("Trip: %s, Guide: %d, Departure: %s, Destination: %s, Duration: %d hours",
                dto.getName(), dto.getLocalguideId(), dto.getDepartureTime(), dto.getDestination(), dto.getDurationInHours());
    }

    private String createContentFromMeetup(MeetupDTO dto) {
        return String.format("Meetup: %s, Location: %s, Date: %s, Cost: %.2f",
                dto.getName(), dto.getLocation(), dto.getDate().toString(), dto.getCost());
    }

    private boolean checkUserExists(Long userId) {
        return Boolean.TRUE.equals(this.webClient.get()
                .uri("http://user-service:8084/api/user/exists/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block());
    }

}
