package com.localeconnect.app.feed.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.localeconnect.app.feed.model.Comment;
import com.localeconnect.app.feed.model.Like;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PostDTO implements Serializable{

    private Long id;

    @NotNull(message = "This is a required field")
    private Long authorID;

    @NotNull(message = "This is a required field")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime date;

    @NotBlank(message = "This is a required field")
    private String content;

    private List<String> images= new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    @NotNull(message = "Post type cannot be null and must be ITINERARY; REGULAR; TRIP; MEETUP")
    private String postType;

    private List<Like> likes = new ArrayList<>();

}
