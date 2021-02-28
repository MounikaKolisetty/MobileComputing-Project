package com.rescuer.api.web.controllers;

import com.rescuer.api.dtos.requestDtos.UserInputDto;
import com.rescuer.api.dtos.responseDtos.TokenDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.repository.rescuer.UserRepository;
import com.rescuer.api.service.UserService;
import com.rescuer.api.web.error.BadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserService userService;

    public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(auth) || ObjectUtils.isEmpty(auth.getPrincipal())) {
            log.error("Unable to get principle from auth object, is auth null? {} ", ObjectUtils.isEmpty(auth));
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body((User) auth.getPrincipal());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserInputDto user) {
        try {
            User userResponse = userService.saveUserDetails(user);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } catch (BadRequest b) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(b.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TokenDto tokenDto = userService.getRefreshToken(auth);
        return ResponseEntity.status(HttpStatus.OK).body(tokenDto.toString());
    }
}
