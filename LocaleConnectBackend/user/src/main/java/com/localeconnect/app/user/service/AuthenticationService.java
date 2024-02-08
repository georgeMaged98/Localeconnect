package com.localeconnect.app.user.service;


import com.localeconnect.app.user.auth.AuthenticationResponse;
import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.localeconnect.app.user.auth.AuthenticationRequest;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.Map;
//import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    //private final WebClient webClient;
    private final UserService userService;

    public AuthenticationResponse registerTraveler(TravelerDTO traveler) {

        log.info("************entred  SERVICE REGISTER-TRAVELER**************");
        TravelerDTO registeredTraveler = userService.registerTraveler(traveler);
        String accessToken = jwtUtil.generateToken(registeredTraveler.getUserName());
        return new AuthenticationResponse(accessToken);
    }

    public AuthenticationResponse registerLocalguide(LocalguideDTO localguide) {

        LocalguideDTO registeredLocalGuide = userService.registerLocalguide(localguide);
        String accessToken = jwtUtil.generateToken(registeredLocalGuide.getUserName());
        return new AuthenticationResponse(accessToken);

    }

    public String login(AuthenticationRequest request) {
        log.info("************entred AUTHSERVICE.LOGIN **************");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        log.info("************finished WITH AUTH MANAGER**************");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("************finished SET AUTHENTICATION**************");
        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(request.getEmail());
            log.info("************GENERATED TOKEN**************");
            jwtUtil.validateToken(token);
            log.info("************Validated TOKEN**************");
            return token;
        } else {
            throw new RuntimeException("Authentication failed");
        }
    }

}
