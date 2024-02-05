package com.localeconnect.app.feed.repository;

import com.localeconnect.app.feed.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
