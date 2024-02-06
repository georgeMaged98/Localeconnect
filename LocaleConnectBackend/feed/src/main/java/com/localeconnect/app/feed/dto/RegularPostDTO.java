package com.localeconnect.app.feed.dto;

import com.localeconnect.app.feed.model.Comment;
import com.localeconnect.app.feed.type.PostType;
import java.time.LocalDateTime;
import java.util.List;

public class RegularPostDTO extends PostDTO {

    // Constructor matching super but setting postType to REGULAR
    public RegularPostDTO(Long id, Long authorID, LocalDateTime date, String content, List<String> images, List<Comment> comments) {
        super(id, authorID, date, content, images, comments, PostType.REGULAR);
    }

    // If you need a no-args constructor as well, make sure it sets the postType to REGULAR
    public RegularPostDTO() {
        super.setPostType(PostType.REGULAR);
    }

    // Ensure postType is always REGULAR for instances of this class
    @Override
    public void setPostType(PostType postType) {
        super.setPostType(PostType.REGULAR);
    }
}
