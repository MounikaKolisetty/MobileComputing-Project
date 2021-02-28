package com.rescuer.api.service;

import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.dtos.responseDtos.TokenDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.entity.UserType;
import com.rescuer.api.repository.rescuer.UserRepository;
import com.rescuer.api.web.error.BadRequest;
import com.rescuer.api.web.util.JWTHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${security.config.secret}")
    private String secret;

    @Value("${security.config.expirationtime}")
    private long expirationTime;

    @Value("${security.config.reftreshexpirationtime}")
    private long refreshExpirationTime;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User saveUserDetails(UserInputDto user) throws IllegalArgumentException, BadRequest {
        User savedUser = new User();
        try {
            log.info("request received to save a user with username: {}", user.getUserName());
            User saveUser = User.getUserFromUser(user, bCryptPasswordEncoder);
            Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());
            if (optionalUser.isPresent()) {
                log.info("adding duplicated username username: {}", user.getUserName());
                throw new BadRequest("A user already exists with the username {}" + user.getUserName());
            }
            saveUser.setUserType(UserType.ADMIN);
            saveUser.setIsUserActive(true);
            return userRepository.save(saveUser);
        } catch (IllegalArgumentException ex) {
            log.error(ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    public TokenDto getRefreshToken(Authentication auth) {
        log.info("request received to refresh token");
        User user = (com.rescuer.api.entity.User) auth.getPrincipal();
        String tokenId = JWTHelper
                .getJWTtoken(user, expirationTime, secret);
        String refreshToken = JWTHelper
                .getJWTtoken(user, refreshExpirationTime, secret);
        return TokenDto.builder().tokenId(tokenId).refreshToken(refreshToken).build();
    }
}
