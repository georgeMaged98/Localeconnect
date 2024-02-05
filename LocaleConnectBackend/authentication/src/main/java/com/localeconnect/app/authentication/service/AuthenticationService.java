package com.localeconnect.app.authentication.service;

import com.localeconnect.app.authentication.auth.AuthenticationRequest;
import com.localeconnect.app.authentication.auth.AuthenticationResponse;
import com.localeconnect.app.authentication.dto.LocalguideDTO;
import com.localeconnect.app.authentication.dto.TravelerDTO;
import com.localeconnect.app.authentication.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final WebClient webClient;

    public AuthenticationResponse registerTraveler(TravelerDTO traveler) {

        traveler.setPassword(BCrypt.hashpw(traveler.getPassword(), BCrypt.gensalt()));
        TravelerDTO registeredTraveler  = webClient.post()
                .uri("http://user-service/api/user/register-traveler")
                .bodyValue(traveler)
                .retrieve()
                .bodyToMono(TravelerDTO.class)
                .block();

        if (registeredTraveler != null) {
            String accessToken = jwtUtil.generateToken(registeredTraveler.getUserName(), registeredTraveler.getEmail());
            return new AuthenticationResponse(accessToken);
        }
        throw new InternalAuthenticationServiceException("error during registration");
    }

    public AuthenticationResponse registerLocalguide(LocalguideDTO localguide) {

        localguide.setPassword(BCrypt.hashpw(localguide.getPassword(), BCrypt.gensalt()));
        LocalguideDTO registeredLocalGuide  = webClient.post()
                .uri("http://user-service/api/user/register-localguide")
                .bodyValue(localguide)
                .retrieve()
                .bodyToMono(LocalguideDTO.class)
                .block();

        if (registeredLocalGuide != null) {
            String accessToken = jwtUtil.generateToken(registeredLocalGuide.getUserName(), registeredLocalGuide.getEmail());
            return new AuthenticationResponse(accessToken);
        }
        throw new InternalAuthenticationServiceException("error during registration");
    }

     public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        UserDTO loggedInUser = webClient.post()
                .uri("http://user-service/api/user/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(loggedInUser.getUserName(), loggedInUser.getEmail());
        return new AuthenticationResponse(token);

    }
}
