package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        try {
            return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
        }
        catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/traveler")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerTraveler(@RequestBody TravelerDTO travelerDTO) {
        try {
            return new ResponseEntity<>(userService.registerTraveler(travelerDTO), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has  occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/register/localguide")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerLocalGuide(@RequestBody LocalguideDTO localguideDTO) {
        try {
            return new ResponseEntity<>(userService.registerLocalguide(localguideDTO), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has  occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        try {
            return new ResponseEntity<>(userService.updateUser(userDTO), HttpStatus.OK);
        }
        catch(UserDoesNotExistException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has  occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/{userId}/follow/{followerId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long userId, @PathVariable("followerId") Long followerId) {
        try {
            userService.followUser(userId, followerId);
            return new ResponseEntity<>("Follow request successful", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error following user: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/unfollow/{followeeId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long userId, @PathVariable("followeeId") Long followeeId) {
        try {
            userService.unfollowUser(userId, followeeId);
            return new ResponseEntity<>("Unfollow successful", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/exists/{userId}")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable("userId") Long userId) {
        boolean exists = userService.checkUserId(userId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
