package com.localeconnect.app.authentication.controller;

import com.localeconnect.app.authentication.auth.AuthenticationRequest;
import com.localeconnect.app.authentication.auth.AuthenticationResponse;
import com.localeconnect.app.authentication.dto.LocalguideDTO;
import com.localeconnect.app.authentication.dto.TravelerDTO;
import com.localeconnect.app.authentication.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationService authenticationService;
    @PostMapping("/register-traveler")
    public ResponseEntity<AuthenticationResponse> registerTraveler(@RequestBody TravelerDTO traveler) {
            return ResponseEntity.ok(authenticationService.registerTraveler(traveler));
    }
    @PostMapping("/register-localguide")
    public ResponseEntity<AuthenticationResponse> registerLocalGuide(@RequestBody LocalguideDTO localguide) {
        return ResponseEntity.ok(authenticationService.registerLocalguide(localguide));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest loginRequest){
            return ResponseEntity.ok(authenticationService.login(loginRequest));
    }
}
