package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.response_handler.ResponseHandler;
import com.localeconnect.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
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

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDTO) {
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

    @GetMapping("/filter-travelers-name")
    public ResponseEntity<Object> searchTravelers(@RequestParam("keyword") String keyword) {
        List<UserDTO> travelers = userService.filterTravelersByFirstLastName(keyword);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, travelers, null);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Object> getFollowers(@PathVariable("userId") Long userId) {
        List<UserDTO> followers = userService.getFollowers(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, followers, null);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Object> getFollowing(@PathVariable("userId") Long userId) {
        List<UserDTO> following = userService.getFollowing(userId);
        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, following, null);
    }
}
