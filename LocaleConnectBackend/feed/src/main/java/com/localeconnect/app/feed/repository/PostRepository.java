package com.localeconnect.app.feed.repository;

import com.localeconnect.app.feed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorID(Long authorId);
}
