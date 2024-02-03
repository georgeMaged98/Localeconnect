package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.repository.UserRepository;
import com.localeconnect.app.user.service.AuthenticationService;
import com.localeconnect.app.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    @PostMapping("/register/traveler")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerTraveler(@RequestBody TravelerDTO travelerDTO) {
        try {
            return new ResponseEntity<>(authenticationService.registerTraveler(travelerDTO), HttpStatus.CREATED);
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
            return new ResponseEntity<>(authenticationService.registerLocalguide(localguideDTO), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has  occured", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
