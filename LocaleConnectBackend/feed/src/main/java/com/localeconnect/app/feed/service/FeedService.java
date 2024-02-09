package com.localeconnect.app.feed.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.localeconnect.app.feed.dto.*;
import com.localeconnect.app.feed.dto.UserFeedDTO;
import com.localeconnect.app.feed.exceptions.LogicException;
import com.localeconnect.app.feed.exceptions.ResourceNotFoundException;
import com.localeconnect.app.feed.mapper.CommentMapper;
import com.localeconnect.app.feed.mapper.LikeMapper;
import com.localeconnect.app.feed.mapper.PostMapper;
import com.localeconnect.app.feed.model.Comment;
import com.localeconnect.app.feed.model.Like;
import com.localeconnect.app.feed.model.Post;
import com.localeconnect.app.feed.repository.CommentRepository;
import com.localeconnect.app.feed.repository.PostRepository;
import com.localeconnect.app.feed.type.PostType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class FeedService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final LikeMapper likeMapper;
    private final WebClient webClient;
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    public PostDTO createPost(RegularPostDTO regularPost){

        Post post = postMapper.toEntity(regularPost);
        long authorId = post.getAuthorID();
        if (!checkUserExists(authorId))
            throw new ResourceNotFoundException("No User Found with id: " + authorId + "!");

        Post createdPost = postRepository.save(post);

        return postMapper.toDomain(createdPost);
    }

    public PostDTO deletePost(Long postId){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();
        postRepository.delete(actualPost);

        return postMapper.toDomain(actualPost);
    }

    public PostDTO addComment(Long postId, CommentDTO commentDTO){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();
        Comment comment = commentMapper.toEntity(commentDTO);

        long authorId = comment.getAuthorID();
        if (!checkUserExists(authorId))
            throw new ResourceNotFoundException("User with id " + authorId + " does not exist!");

        actualPost.addComment(comment);
        postRepository.save(actualPost);

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
    public PostDTO shareTrip(TripDTO trip, Long authorId) {
        Post post = new Post();
        post.setAuthorID(authorId);
        post.setPostType(PostType.TRIP);
        post.setContent(createContentFromTrip(trip));
        post.setDate(LocalDateTime.now());
        post.setImages(trip.getImageUrls());

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }

    public PostDTO shareItinerary(ItineraryDTO itinerary, Long authorId) {
        Post post = new Post();
        post.setAuthorID(authorId);
        post.setPostType(PostType.ITINERARY);
        post.setContent(createContentFromItinerary(itinerary));
        post.setDate(LocalDateTime.now());
        post.setImages(itinerary.getImageUrls());

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }
    public PostDTO shareMeetup(MeetupDTO meetup, Long authorId) {
        Post post = new Post();
        post.setAuthorID(authorId);
        post.setPostType(PostType.MEETUP);
        post.setContent(createContentFromMeetup(meetup));
        post.setDate(LocalDateTime.now());

        Post createdPost = postRepository.save(post);
        return postMapper.toDomain(createdPost);
    }

    public PostDTO getPostById(Long postId) {
        Post postToFind = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("No Post Found with id: " + postId + "!"));
        return postMapper.toDomain(postToFind);
    }

    public List<String> getPostLikes(Long postId) {
        PostDTO post = getPostById(postId);
        List<String> usersLikedThePost = new ArrayList<>();

        for(Like like : post.getLikes()) {
            usersLikedThePost.add(getUserNameById(like.getLikerId()));
        }
        return usersLikedThePost;
    }

    public PostDTO likePost(Long postId, LikeDTO likeDTO) {
        Post postToFind = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("No Post Found with id: " + postId + "!"));
        Like like = likeMapper.toEntity(likeDTO);

        Long likerId = like.getLikerId();
        if(!checkUserExists(likerId))
            throw new ResourceNotFoundException("User with id " + likerId + " does not exist!");

        postToFind.addLike(like);
        postRepository.save(postToFind);
        return postMapper.toDomain(postToFind);
    }

    public PostDTO unlikePost(Long postId, LikeDTO likeDTO) {
        Post postToFind = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("No Post Found with id: " + postId + "!"));
        Like like = likeMapper.toEntity(likeDTO);

        Long likerId = like.getLikerId();
        if(!checkUserExists(likerId))
            throw new ResourceNotFoundException("User with id " + likerId + " does not exist!");

        postToFind.removeLike(like);
        postRepository.save(postToFind);
        return postMapper.toDomain(postToFind);
    }

    public List<PostDTO> getPostsByAuthor(Long authorId) {
        if(!checkUserExists(authorId))
            throw new ResourceNotFoundException("User with id " + authorId + " does not exist!");

        return postRepository.findByAuthorID(authorId).stream().map(postMapper::toDomain).toList();
    }

    public List<PostDTO> searchPosts(String keyword) {
        return postRepository.findByContentContainingIgnoreCase(keyword)
                .stream().map(postMapper::toDomain).collect(Collectors.toList());
    }

    public List<PostDTO> filterPosts(Long authorID, PostType postType, LocalDateTime startDate, LocalDateTime endDate) {
        List<Post> posts = postRepository.findByAuthorIDAndPostTypeAndDateBetween(authorID, postType, startDate, endDate);
        return posts.stream()
                .map(postMapper::toDomain)
                .collect(Collectors.toList());
    }
    public Mono<List<PostDTO>> generateUserFeed(Long userId) {
        return getFollowing(userId)
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::getPostsByAuthorId)
                .collectList()
                .flatMap(feed -> updateFeedCache(userId, feed).thenReturn(feed));
    }

    private Mono<List<Long>> getFollowing(Long userId) {
        return webClient.get()
                .uri("http://user-service:8084/api/user/secured/{userId}/following", userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Long>>() {});
    }

    private Flux<PostDTO> getPostsByAuthorId(Long authorId) {
        List<PostDTO> result = new ArrayList<>();
        List<Post> posts = postRepository.findByAuthorID(authorId);
        for(Post p : posts) {
            result.add(postMapper.toDomain(p));
        }
        return Flux.fromIterable(result);
    }

    private Mono<Boolean> updateFeedCache(Long userId, List<PostDTO> feed) {
        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(feed))
                .flatMap(serializedFeed -> reactiveRedisTemplate.opsForValue().set("feed:" + userId, serializedFeed));
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
                .uri("http://user-service:8084/api/user/auth/exists/{userId}", userId)
                .retrieve().bodyToMono(Boolean.class).block());
    }

    private String getUserNameById(Long id) {
        if (!checkUserExists(id))
            throw new ResourceNotFoundException("User with id " + id + " does not exist!");

        return this.webClient.get()
                    .uri("http://user-service:8084/api/user/{userId}", id)
                    .retrieve().bodyToMono(UserFeedDTO.class).block().getUserName();
    }

}

