package com.rescuer.api.service;

import com.rescuer.api.entity.User;
import com.rescuer.api.repository.rescuer.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@Slf4j
public class UserDetailsServiceimpl implements AuthenticationProvider {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailsServiceimpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = ((User) authentication.getPrincipal());

        if (user != null) {
            Optional<User> optionalUser = this.userRepository.findByUserName(user.getUsername());
            if (optionalUser.isPresent()) {
                User foundUser = optionalUser.get();
                boolean isValidPassword = this.passwordEncoder.matches(user.getPassword(), foundUser.getPassword());
                if (isValidPassword) {
                    Authentication token = new UsernamePasswordAuthenticationToken(optionalUser.get(), "",
                            foundUser.getAuthorities());
                    return token;
                } else {
                    log.info("Invalid password for userName {}", user.getUsername());
                }
            } else {
                log.info("User not found with userName {}", user.getUsername());
            }
        } else {
            log.info("userName and password cannot be empty");
        }
        // hacker shouldn't be able to guess where it got failed by calculating response time
        Thread.sleep(1000);
        throw new BadCredentialsException("Authentication Failed!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
