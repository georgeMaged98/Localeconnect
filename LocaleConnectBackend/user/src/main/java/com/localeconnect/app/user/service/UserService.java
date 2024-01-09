package com.localeconnect.app.user.service;

import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.localeconnect.app.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
   // @Autowired
   // private JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserDTO> allUsersDTO = allUsers.stream()
                .map(user -> convertToDTO(user))
                .collect(Collectors.toList());

        return allUsersDTO;
    }

    public UserDTO getUserById(Long id) {
        return convertToDTO(userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException("User with the given id does not exist")));
    }
    public User registerUser(UserDTO userDTO) {
        if (userRepository.existsByuserName(userDTO.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists");
        }

        if (userDTO.isRegisteredAsLocalGuide()) {
            if (!(userDTO.getLanguages().size() < 2)
                    || userDTO.getDateOfBirth().plusYears(18).isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Cannot be a local guide: must accept the conditions and" +
                        " speak at least 2 languages and be at least 18 years old.");
            }
        }

            /*sendConfirmationEmail(
                    newLocalGuide.getEmail(),
             "Welcome to LocaleConnect",
              "Dear " + newLocalGuide.getFirstName() + ",\n\nWelcome to LocaleConnect! Your account has been successfully created."
                    );
            */
            return userRepository.save(convertToUser(userDTO));
    }

    public UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .bio(user.getBio())
                .password(user.getPassword())
                .visitedCountries(user.getVisitedCountries())
                .registeredAsLocalGuide(user.isRegisteredAsLocalGuide())
                .languages(user.getLanguages())
                .build();
    }

    public User convertToUser(UserDTO userDTO) {
        return  User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .userName(userDTO.getUserName())
                .email(userDTO.getEmail())
                .dateOfBirth(userDTO.getDateOfBirth())
                .bio(userDTO.getBio())
                .password(userDTO.getPassword())
                .visitedCountries(userDTO.getVisitedCountries())
                .registeredAsLocalGuide(userDTO.isRegisteredAsLocalGuide())
                .languages(userDTO.getLanguages())
                .build();
    }

   /*  public void sendConfirmationEmail (String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
    */
}
