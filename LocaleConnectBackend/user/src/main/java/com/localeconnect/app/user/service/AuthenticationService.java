package com.localeconnect.app.authentication.service;

import com.localeconnect.app.authentication.auth.AuthenticationRequest;
import com.localeconnect.app.authentication.auth.AuthenticationResponse;
import com.localeconnect.app.authentication.dto.LocalguideDTO;
import com.localeconnect.app.authentication.dto.TravelerDTO;
import com.localeconnect.app.authentication.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    //private final WebClient webClient;

    public AuthenticationResponse registerTraveler(TravelerDTO traveler) {

/*        TravelerDTO registeredTraveler  = webClient.post()
                .uri("http://user-service:8084/api/user/register-traveler")
                .bodyValue(traveler)
                .retrieve()
                .bodyToMono(TravelerDTO.class)
                .block();*/
        log.info("************entred  SERVICE REGISTER-TRAVELER**************");
        TravelerDTO registeredTraveler = restTemplate.postForObject("http://user-service:8084/api/user/register-traveler", traveler, TravelerDTO.class);
        String accessToken = jwtUtil.generateToken(registeredTraveler.getUserName());
        return new AuthenticationResponse(accessToken);
    }

    public AuthenticationResponse registerLocalguide(LocalguideDTO localguide) {

/*        LocalguideDTO registeredLocalGuide  = webClient.post()
                .uri("http://user-service:8084/api/user/register-localguide")
                .bodyValue(localguide)
                .retrieve()
                .bodyToMono(LocalguideDTO.class)
                .block();*/
        LocalguideDTO registeredLocalGuide = restTemplate.postForObject("http://user-service:8084/api/user/register-localguide", localguide, LocalguideDTO.class);

        String accessToken = jwtUtil.generateToken(registeredLocalGuide.getUserName());
        return new AuthenticationResponse(accessToken);

    }

     public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        /*UserDTO loggedInUser = webClient.post()
                .uri("http://user-service:8084/api/user/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();*/
         UserDTO loggedInUser = restTemplate.postForObject("http://user-service:8084/api/user/login", request, UserDTO.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.generateToken(loggedInUser.getUserName());
        return new AuthenticationResponse(token);

    }
}
