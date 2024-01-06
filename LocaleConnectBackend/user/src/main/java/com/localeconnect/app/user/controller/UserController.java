package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.service.UserService;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {
            return new ResponseEntity<>(userService.registerUser(userDTO), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has  occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
