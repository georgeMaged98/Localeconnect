package com.localeconnect.app.authentication.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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
    @NotBlank(message = "This is a required field")
    private String password;
    private List<String> visitedCountries;
    @NotNull(message = "This is a required field")
    private boolean registeredAsLocalGuide;
    private List<String> languages;
    private List<Long> followerIds;
    private List<Long> followingIds;

}
