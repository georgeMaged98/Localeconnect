package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.response_handler.ResponseHandler;
import com.localeconnect.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, users, null);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable("userId") Long userId) {
        UserDTO user = userService.getUserById(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, user, null);
    }

    @PostMapping("/register-traveler")
    public ResponseEntity<Object> registerTraveler(@RequestBody TravelerDTO travelerDTO) {
        TravelerDTO registeredTraveler = userService.registerTraveler(travelerDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, registeredTraveler, null);
    }

    @PostMapping("/register-localguide")
    public ResponseEntity<Object> registerLocalGuide(@RequestBody LocalguideDTO localguideDTO) {
        LocalguideDTO registeredLocalguide = userService.registerLocalguide(localguideDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, registeredLocalguide, null);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userDTO);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, updatedUser, null);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{userId}/follow/{followerId}")
    public ResponseEntity<Object> followUser(@PathVariable("userId") Long userId, @PathVariable("followerId") Long followerId) {
        userService.followUser(userId, followerId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{userId}/unfollow/{followeeId}")
    public ResponseEntity<Object> unfollowUser(@PathVariable("userId") Long userId, @PathVariable("followeeId") Long followeeId) {
        userService.unfollowUser(userId, followeeId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, null, null);
    }

    @PostMapping("/{guideId}/rate/{travelerId}")
    public ResponseEntity<Object> rateLocalGuide(@PathVariable("guideId") Long guideId,
                                                 @PathVariable("travelerId") Long travelerId,
                                                 @RequestBody Double rating) {
        LocalguideDTO ratedLocalGuide = userService.rateLocalGuide(guideId, travelerId, rating);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, ratedLocalGuide, null);
    }

    @GetMapping("/exists/{userId}")
    public ResponseEntity<Object> checkUserExists(@PathVariable("userId") Long userId) {
        boolean exists = userService.checkUserId(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, exists, null);
    }
}
