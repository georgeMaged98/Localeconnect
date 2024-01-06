package com.localeconnect.app.user.service;

import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.model.LocalGuide;
import com.localeconnect.app.user.model.Traveler;
import com.localeconnect.app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.localeconnect.app.user.repository.UserRepository;

import java.time.LocalDate;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public User registerUser(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        if(userRepository.existsByEmail(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists");
        }

        if(userDTO.isRegisterAsLocalGuide()){
            if(!userDTO.isAcceptsConditions() || userDTO.getLanguages().size() < 2
            || userDTO.getDateOfBirth().plusYears(18).isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Cannot be a local guide: must accept the conditions and" +
                        " speak at least 2 languages and be at least 18 years old.");
            }

            LocalGuide newLocalGuide = new LocalGuide(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getUserName(),
                    userDTO.getEmail(),
                    userDTO.getDateOfBirth(),
                    userDTO.getBio(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    userDTO.getLanguages(),
                    userDTO.isAcceptsConditions()
            );

            sendConfirmationEmail(
                    newLocalGuide.getEmail(),
             "Welcome to LocaleConnect",
              "Dear " + newLocalGuide.getFirstName() + ",\n\nWelcome to LocaleConnect! Your account has been successfully created."
                    );

            return userRepository.save(newLocalGuide);

        } else {
            // Logic for regular Traveler
            Traveler newTraveler = new Traveler(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getUserName(),
                    userDTO.getEmail(),
                    userDTO.getDateOfBirth(),
                    userDTO.getBio(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    null  // Assuming no visitedCountries data is provided at registration
            );
            sendConfirmationEmail(
                    newTraveler.getEmail(),
                    "Welcome to LocaleConnect",
                    "Dear " + newTraveler.getFirstName() + ",\n\nWelcome to LocaleConnect! Your account has been successfully created."
            );
            return userRepository.save(newTraveler);

        }
    }

    public void sendConfirmationEmail (String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
