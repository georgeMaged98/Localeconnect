package com.localeconnect.app.feed.dto;

import com.localeconnect.app.feed.model.Comment;
import java.time.LocalDateTime;
import java.util.List;

public class RegularPostDTO extends PostDTO {

    public RegularPostDTO() {
        super.setPostType("REGULAR");
    }
}
