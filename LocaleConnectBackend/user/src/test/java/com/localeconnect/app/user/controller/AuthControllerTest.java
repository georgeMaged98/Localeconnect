package com.localeconnect.app.user.controller;


import com.localeconnect.app.user.auth.AuthenticationRequest;
import com.localeconnect.app.user.auth.AuthenticationResponse;
import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.service.AuthenticationService;
import com.localeconnect.app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationService authService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterTraveler() {

        TravelerDTO travelerDTO = new TravelerDTO();
        travelerDTO.setUserName("testUser");
        travelerDTO.setEmail("test@example.com");
        travelerDTO.setPassword("password");

        AuthenticationResponse expectedAuthResponse = new AuthenticationResponse("dummy-token-for-traveler");
        when(authService.registerTraveler(any(TravelerDTO.class))).thenReturn(expectedAuthResponse);

        ResponseEntity<Object> response = authController.registerTraveler(travelerDTO);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        AuthenticationResponse actualResponseObject = (AuthenticationResponse) responseBody.get("data");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuthResponse.getAccessToken(), actualResponseObject.getAccessToken());

        verify(authService).registerTraveler(travelerDTO);
    }

    @Test
    void testRegisterLocalGuide() {
        LocalguideDTO localguideDTO = new LocalguideDTO();
        localguideDTO.setUserName("localGuideUser");
        localguideDTO.setEmail("localguide@example.com");
        localguideDTO.setPassword("securePassword");
        localguideDTO.setLanguages(List.of("English", "Spanish"));
        localguideDTO.setDateOfBirth(LocalDate.now().minusYears(20));

        AuthenticationResponse expectedAuthResponse = new AuthenticationResponse("dummy-token-for-localguide");
        when(authService.registerLocalguide(any(LocalguideDTO.class))).thenReturn(expectedAuthResponse);

        ResponseEntity<Object> response = authController.registerLocalGuide(localguideDTO);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        AuthenticationResponse actualResponseObject = (AuthenticationResponse) responseBody.get("data");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedAuthResponse.getAccessToken(), actualResponseObject.getAccessToken());

        verify(authService).registerLocalguide(localguideDTO);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationRequest loginRequest = new AuthenticationRequest("user@example.com", "password");
        String expectedToken = "auth-token";

        when(authService.login(loginRequest)).thenReturn(expectedToken);

        ResponseEntity<Object> response = authController.login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(expectedToken, responseBody.get("data"));

        verify(authService).login(loginRequest);
    }

    @Test
    void testLoginFailure() {
        AuthenticationRequest loginRequest = new AuthenticationRequest("user@example.com", "wrongPassword");

        when(authService.login(loginRequest)).thenThrow(new UserDoesNotExistException("False credentials! Please try to login again."));

        Exception exception = assertThrows(UserDoesNotExistException.class, () -> {
            authController.login(loginRequest);
        });

        String expectedMessage = "False credentials! Please try to login again.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testCheckUserExistsTrue() {
        Long userId = 1L;
        when(userService.checkUserId(userId)).thenReturn(true);

        ResponseEntity<Object> response = authController.checkUserExists(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(true, responseBody.get("data"));

        verify(userService).checkUserId(userId);
    }

    @Test
    void testCheckUserExistsFalse() {
        Long userId = 2L;
        when(userService.checkUserId(userId)).thenReturn(false);

        ResponseEntity<Object> response = authController.checkUserExists(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals(false, responseBody.get("data"));

        verify(userService).checkUserId(userId);
    }


}

