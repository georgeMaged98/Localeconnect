package com.localeconnect.app.user.service;

import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.localeconnect.app.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

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

            sendConfirmationEmail(userDTO);
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

    public void sendConfirmationEmail(UserDTO userDTO) {
        sendEmail(userDTO.getEmail(),"Welcome to LocaleConnect!", getConfirmationMailBody(userDTO), true);
    }

    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        try{
            if (isHtml) {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                helper.setText(body, true); // true indicates HTML content
                helper.setTo(to);
                helper.setSubject(subject);

                mailSender.send(mimeMessage);
            } else {
                message.setText(body);
                mailSender.send(message);
            }
        } catch (MessagingException e) {
            log.error("Error sending email", e);
            throw new RuntimeException("Error sending email", e);
        }
    }

    private String getConfirmationMailBody(UserDTO userDTO) {
        return   "<html><body>"
                + "<h1>Welcome to LocaleConnect, " + userDTO.getFirstName() + "!</h1>"
                + "<p>We're excited to have you on board. Your account has been successfully created.</p>"
                + "<p>Here are some next steps to get you started:</p>"
                + "<ul>"
                + "<li>Explore our <a href='URL_TO_FEATURES'>features</a>.</li>"
                + "<li>Set up your <a href='URL_TO_PROFILE'>profile</a>.</li>"
                + "</ul>"
                + "<p>If you have any questions, feel free to contact our support team.</p>"
                + "<p>Sincerely,<br>Your LocaleConnect Team</p>"
                + "<hr>"
                + "<footer>"
                + "<p><a href='URL_TO_PRIVACY_POLICY'>Privacy Policy</a> | <a href='URL_TO_TERMS'>Terms of Service</a></p>"
                + "</footer>"
                + "</body></html>";
    }
}
