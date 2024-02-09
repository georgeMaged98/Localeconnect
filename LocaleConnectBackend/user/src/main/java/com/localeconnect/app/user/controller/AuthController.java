package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.auth.AuthenticationRequest;
import com.localeconnect.app.user.auth.AuthenticationResponse;
import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.response_handler.ResponseHandler;
import com.localeconnect.app.user.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/user/auth")
public class AuthController {
    private final AuthenticationService authService;


    @PostMapping("/register-traveler")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerTraveler(@RequestBody @Valid TravelerDTO travelerDTO) {
        log.info("************entred api/user/register-traveler request**************");
        AuthenticationResponse response = authService.registerTraveler(travelerDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, response, null);
    }

    @PostMapping("/register-localguide")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registerLocalGuide(@RequestBody @Valid LocalguideDTO localguideDTO) {
        AuthenticationResponse response = authService.registerLocalguide(localguideDTO);

        return ResponseHandler.generateResponse("Success!", HttpStatus.OK, response, null);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationRequest request) {
        log.info("************entred AUTH/LOGIN CONTROLLER**************");
        try {
            String token = authService.login(request);
            return ResponseEntity.ok().body(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }

    }
}
