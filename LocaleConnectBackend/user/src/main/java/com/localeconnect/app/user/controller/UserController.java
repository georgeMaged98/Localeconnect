package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.model.Localguide;
import com.localeconnect.app.user.model.User;
import com.localeconnect.app.user.response_handler.ResponseHandler;
import com.localeconnect.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user/secured")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() {
        log.info("************entred GETALLUSERS USER CONTROLLER**************");
        List<UserDTO> users = userService.getAllUsers();

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, users, null);
    }
    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable("userId") Long userId) {
            return userService.getUserById(userId);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO userFound = userService.updateUser(userDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, userFound, null);
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{followerId}/follow/{userId}")
    public ResponseEntity<Object> followUser(@PathVariable("followerId") Long followerId, @PathVariable("userId") Long userId) {
        userService.followUser(followerId, userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{followerId}/unfollow/{userId}")
    public ResponseEntity<Object> unfollowUser(@PathVariable("followerId") Long followerId, @PathVariable("userId") Long userId) {
        userService.unfollowUser(followerId, userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{guideId}/rate/{travelerId}")
    public ResponseEntity<Object> rateLocalGuide(@PathVariable("guideId") Long guideId,
                                                 @PathVariable("travelerId") Long travelerId,
                                                 @RequestParam("rating") Double rating) {
        LocalguideDTO ratedLocalGuide = userService.rateLocalGuide(guideId, travelerId, rating);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, ratedLocalGuide, null);
    }
    @GetMapping("/guides")
    public ResponseEntity<Object> getAllGuides() {
        List<LocalguideDTO> guides = userService.getAllGuides();

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, guides, null);
    }

    @GetMapping("/filter-guides-city")
    public ResponseEntity<Object> filterLocalGuideByCity(@RequestParam("keyword") String keyword) {
        List<LocalguideDTO> guides = userService.filterLocalGuideByCity(keyword);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, guides, null);
    }

    @GetMapping("/search-traveler")
    public ResponseEntity<Object> searchTravelers(@RequestParam("keyword") String keyword) {
        List<UserDTO> travelers = userService.searchTravelersByFirstLastName(keyword);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, travelers, null);
    }

    @GetMapping("/{userId}/followers")
    public List<UserDTO> getFollowers(@PathVariable("userId") Long userId) {
        List<UserDTO> followers = userService.getFollowers(userId);

        return followers;
    }

    @GetMapping("/{userId}/following")
    public List<UserDTO> getFollowing(@PathVariable("userId") Long userId) {
        List<UserDTO> following = userService.getFollowing(userId);

        return following;
    }
    @GetMapping("/{userId}/profile")
    public ResponseEntity<Object> getProfile(@PathVariable("userId") Long userId) {
        UserDTO userFound = userService.getProfileDetails(userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, userFound, null);
    }

    @GetMapping("/{userId}/rating")
    public ResponseEntity<Object> getAverageRatingOfLocalGuide(@PathVariable("userId") Long userId) {
        double averageRating = userService.getAverageRatingOfLocalGuide(userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, averageRating, null);
    }

    @GetMapping("/{userId}/rating-count")
    public ResponseEntity<Object> getRatingCountOfLocalGuide(@PathVariable("userId") Long userId) {
        int ratingCount = userService.getRatingCountOfLocalGuide(userId);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, ratingCount, null);
    }
}
