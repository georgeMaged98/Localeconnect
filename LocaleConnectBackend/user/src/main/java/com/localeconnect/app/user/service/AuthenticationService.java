package com.localeconnect.app.user.service;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.mapper.LocalguideMapper;
import com.localeconnect.app.user.mapper.TravelerMapper;
import com.localeconnect.app.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final UserConfirmationEmail userConfirmationEmail;
    private final TravelerMapper travelerMapper;
    private final LocalguideMapper localguideMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        if (userRepository.existsByUserName(travelerDTO.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        if (userRepository.existsByEmail(travelerDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists");
        }

        userConfirmationEmail.sendConfirmationEmail(travelerDTO);
        userRepository.save(travelerMapper.toEntity(travelerDTO));

        return travelerDTO;
    }

    public LocalguideDTO registerLocalguide(LocalguideDTO localguideDTO) {
        if (userRepository.existsByUserName(localguideDTO.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        if (userRepository.existsByEmail(localguideDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists");
        }

        if (!(localguideDTO.getLanguages().size() < 2)
                || localguideDTO.getDateOfBirth().plusYears(18).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot be a local guide: must accept the conditions and" +
                    " speak at least 2 languages and be at least 18 years old.");
        }

        userConfirmationEmail.sendConfirmationEmail(localguideDTO);
        userRepository.save(localguideMapper.toEntity(localguideDTO));

        return localguideDTO;
    }

}
