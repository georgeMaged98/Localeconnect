package com.localeconnect.app.user.service;

import com.localeconnect.app.user.dto.LocalguideDTO;
import com.localeconnect.app.user.dto.TravelerDTO;
import com.localeconnect.app.user.dto.UserDTO;
import com.localeconnect.app.user.dto.UserPrincipalDTO;
import com.localeconnect.app.user.exception.UserAlreadyExistsException;
import com.localeconnect.app.user.exception.UserDoesNotExistException;
import com.localeconnect.app.user.mapper.LocalguideMapper;
import com.localeconnect.app.user.mapper.TravelerMapper;
import com.localeconnect.app.user.mapper.UserMapper;
import com.localeconnect.app.user.model.Localguide;
import com.localeconnect.app.user.model.Traveler;
import com.localeconnect.app.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import com.localeconnect.app.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserConfirmationEmail userConfirmationEmail;
    private final UserMapper userMapper;
    private final TravelerMapper travelerMapper;
    private final LocalguideMapper localguideMapper;
    public TravelerDTO registerTraveler(TravelerDTO travelerDTO) {
        log.info("************entred USERSERVICE/register-traveler **************");
        if (userRepository.existsByUserName(travelerDTO.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists.");
        }

        if (userRepository.existsByEmail(travelerDTO.getEmail())) {
            throw new UserAlreadyExistsException("A user with the given email already exists");
        }

        userConfirmationEmail.sendConfirmationEmail(travelerDTO);
        Traveler traveler = travelerMapper.toEntity(travelerDTO);
        traveler.setPassword(BCrypt.hashpw(traveler.getPassword(), BCrypt.gensalt()));
        userRepository.save(traveler);

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
        Localguide localguide = localguideMapper.toEntity(localguideDTO);
        localguide.setPassword(BCrypt.hashpw(localguide.getPassword(), BCrypt.gensalt()));
        userRepository.save(localguide);

        return localguideDTO;
    }

    public List<UserDTO> getAllUsers() {
        log.info("************entred USERSERVICE GETALLUSERS CONTROLLER**************");
        List<User> allUsers = userRepository.findAll();
        log.info("************finished fetching ALLUSERS **************");
        return allUsers.stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException("User with the given id does not exist"));
        return userMapper.toDomain(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
        userRepository.delete(user);
    }
    public void followUser(Long userId, Long followerId) {
        User userToFollow = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new UserDoesNotExistException("Follower not found"));
        if(userId.equals(followerId)) {
            throw new IllegalArgumentException("User cannot unfollow themselves");
        }
        if (!userToFollow.getFollowers().contains(follower)) {
            userToFollow.getFollowers().add(follower);
            userRepository.save(userToFollow);
            // ToDo Notify the user about the new follower
        } else {
            throw new IllegalStateException("Already following this user");
        }
    }
    public void unfollowUser(Long userId, Long followeeId) {
        if(userId.equals(followeeId)) {
            throw new IllegalArgumentException("User cannot unfollow themselves");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User not found"));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new UserDoesNotExistException("Followee not found"));

        if(user.getFollowing().contains(followee)) {
            user.getFollowing().remove(followee);
            followee.getFollowers().remove(user);

            userRepository.save(user);
            userRepository.save(followee);
        } else {
            throw new IllegalStateException("User is not following the specified followee");
        }
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new UserDoesNotExistException("User with email " + userDTO.getEmail() + " does not exist"));

        userMapper.updateUserFromDto(userDTO, existingUser);
        User savedUser = userRepository.save(existingUser);
        return userMapper.toDomain(savedUser);
    }

    public LocalguideDTO rateLocalGuide(Long guideId, Long travelerId, Double rating) {
        Localguide localguide = userRepository.findById(guideId)
                .filter(user -> user instanceof Localguide)
                .map(user -> (Localguide) user)
                .orElseThrow(() -> new UserDoesNotExistException("LocalGuide with id " + guideId + " does not exist"));

        if(!checkUserId(travelerId))
            throw new UserDoesNotExistException("Traveler with id " + travelerId + " does not exist");

        localguide.addRating(rating);

        userRepository.save(localguide);
        LocalguideDTO ratedLocalGuideDTO = localguideMapper.toDomain(localguide);
        ratedLocalGuideDTO.setAverageRating(localguide.getAverageRating());
        return ratedLocalGuideDTO;
    }

    public List<LocalguideDTO> getAllGuides() {
        List<Localguide> guides = userRepository.findByRegisteredAsLocalGuide(true).stream()
                .filter(Localguide.class::isInstance)
                .map(Localguide.class::cast)
                .collect(Collectors.toList());
        return guides.stream()
                .map(localguideMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<LocalguideDTO> filterLocalGuideByCity(String keyword) {
        List<Localguide> guides = userRepository.findByCityContainingIgnoreCase(keyword).stream()
                .filter(Localguide.class::isInstance)
                .map(Localguide.class::cast)
                .collect(Collectors.toList());
        return guides.stream()
                .map(localguideMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserDTO> filterTravelersByFirstLastName(String keyword) {
        List<User> travelers = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword).stream()
                .filter(user -> !user.isRegisteredAsLocalGuide())
                .collect(Collectors.toList());
        return travelers.stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with the id " + userId + "does not Exist!"));
        return user.getFollowers().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException("User with the id " + userId + "does not Exist!"));
        return user.getFollowing().stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    public boolean checkUserId(Long userId) {
        return userRepository.findById(userId).isPresent();
    }

    @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("user not found with email :" + email);
            } else {
                return new UserPrincipalDTO(user.get());
        }
    }
}