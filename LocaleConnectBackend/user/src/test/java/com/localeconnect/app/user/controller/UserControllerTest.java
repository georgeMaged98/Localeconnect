package com.localeconnect.app.user.controller;

import com.localeconnect.app.user.auth.AuthenticationResponse;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.model.User;
import com.localeconnect.app.user.service.AuthenticationService;
import com.localeconnect.app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetUserById() {
        Long userId = 1L;
        UserDTO mockUser = new UserDTO();
        mockUser.setId(userId);
        mockUser.setUserName("Test");
        mockUser.setEmail("Test1@example.com");
        mockUser.setFirstName("Test");
        mockUser.setLastName("Last");

        when(userService.getUserById(userId)).thenReturn(mockUser);

        ResponseEntity<Object> response = userController.getUserById(userId);

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        UserDTO actualResponseObject = (UserDTO) responseBody.get("responseObject");


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(userId, actualResponseObject.getId());
        assertEquals("Test", actualResponseObject.getUserName());
        assertEquals("Test1@example.com", actualResponseObject.getEmail());
        assertEquals("Test", actualResponseObject.getFirstName());
        assertEquals("Last", actualResponseObject.getLastName());

        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetAllUsers() {
        List<UserDTO> mockUsers = new ArrayList<>();
        // User1
        Long userId = 1L;
        UserDTO mockUser1 = new UserDTO();
        mockUser1.setId(userId);
        mockUser1.setUserName("Test1");
        mockUser1.setEmail("Test1@example.com");
        mockUser1.setFirstName("Test1");
        mockUser1.setLastName("Last1");

        // User2
        Long secondUserId = 2L;
        UserDTO mockUser2 = new UserDTO();
        mockUser2.setId(secondUserId);
        mockUser2.setUserName("Test2");
        mockUser2.setEmail("Test2@example.com");
        mockUser2.setFirstName("Test2");
        mockUser2.setLastName("Last2");

        mockUsers.add(mockUser1);
        mockUsers.add(mockUser2);

        when(userService.getAllUsers()).thenReturn(mockUsers);
        ResponseEntity<Object> response = userController.getAllUsers();

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        List<UserDTO> actualResponseObject = (List<UserDTO>) responseBody.get("responseObject");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(2, actualResponseObject.size());
        assertEquals(mockUsers.get(0).getEmail(), actualResponseObject.get(0).getEmail());
        assertEquals(mockUsers.get(1).getEmail(), actualResponseObject.get(1).getEmail());

        verify(userService, times(1)).getAllUsers();
    }
    @Test
    void testUpdateUser() {
        UserDTO mockUserToUpdate = new UserDTO();
        mockUserToUpdate.setId(1L);
        mockUserToUpdate.setUserName("UpdatedUser");
        mockUserToUpdate.setEmail("updateduser@example.com");
        mockUserToUpdate.setFirstName("Updated");
        mockUserToUpdate.setLastName("User");

        when(userService.updateUser(any(UserDTO.class))).thenReturn(mockUserToUpdate);

        ResponseEntity<Object> response = userController.updateUser(mockUserToUpdate);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        UserDTO actualResponseObject = (UserDTO) responseBody.get("responseObject");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());


        assertEquals("UpdatedUser", actualResponseObject.getUserName());
        assertEquals("updateduser@example.com", actualResponseObject.getEmail());

        verify(userService, times(1)).updateUser(any(UserDTO.class));
    }

    @Test
    void testDeleteUser() {
        Long userIdToDelete = 1L;
        doNothing().when(userService).deleteUser(userIdToDelete);
        ResponseEntity<Object> response = userController.deleteUser(userIdToDelete);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService, times(1)).deleteUser(userIdToDelete);
    }
    @Test
    void testFollowUser() {
        Long followerId = 1L;
        Long userIdToFollow = 2L;

        doNothing().when(userService).followUser(followerId, userIdToFollow);

        ResponseEntity<Object> response = userController.followUser(followerId, userIdToFollow);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService, times(1)).followUser(followerId, userIdToFollow);
    }

    @Test
    void testUnfollowUser() {
        Long followerId = 1L;
        Long userIdToUnfollow = 2L;

        doNothing().when(userService).unfollowUser(followerId, userIdToUnfollow);

        ResponseEntity<Object> response = userController.unfollowUser(followerId, userIdToUnfollow);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userService, times(1)).unfollowUser(followerId, userIdToUnfollow);
    }


}
