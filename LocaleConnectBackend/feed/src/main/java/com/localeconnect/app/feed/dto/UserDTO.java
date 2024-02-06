package com.localeconnect.app.feed.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDTO {

    @NotBlank(message = "This is a required field")
    private String firstName;
    @NotBlank(message = "This is a required field")
    private String lastName;
    @NotBlank(message = "This is a required field")
    private String userName;
    @NotBlank(message = "This is a required field")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "This is a required field")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private String bio;
    @Setter(AccessLevel.NONE)
    @NotBlank(message = "This is a required field")
    private String password;
    private List<String> visitedCountries;
    @NotNull(message = "This is a required field")
    private boolean registeredAsLocalGuide;
    private List<String> languages;
    private List<Long> followerIds;
    private List<Long> followingIds;

    // Setter method for password with hash logic
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    // Private method for password hashing
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

}
