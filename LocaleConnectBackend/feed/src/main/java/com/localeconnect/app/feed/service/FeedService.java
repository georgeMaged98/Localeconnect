package com.localeconnect.app.feed.service;

import com.localeconnect.app.feed.dto.CommentDTO;
import com.localeconnect.app.feed.dto.PostDTO;
import com.localeconnect.app.feed.exceptions.ResourceNotFoundException;
import com.localeconnect.app.feed.mapper.CommentMapper;
import com.localeconnect.app.feed.mapper.PostMapper;
import com.localeconnect.app.feed.model.Comment;
import com.localeconnect.app.feed.model.Post;
import com.localeconnect.app.feed.repository.CommentRepository;
import com.localeconnect.app.feed.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    public PostDTO createPost(PostDTO postDTO){

        Post post = postMapper.toEntity(postDTO);
        Post createdPost = postRepository.save(post);

        // return saved post
        return postMapper.toDomain(createdPost);
    }

    public PostDTO addComment(Long postId, CommentDTO commentDTO){
        Optional<Post> optional = postRepository.findById(postId);
        if (optional.isEmpty())
            throw new ResourceNotFoundException("No Post Found with id: " + postId + "!");

        Post actualPost = optional.get();

        Comment comment = commentMapper.toEntity(commentDTO);
        actualPost.addComment(comment);

        postRepository.save(actualPost);
        // return saved post
        return postMapper.toDomain(actualPost);
    }


}
