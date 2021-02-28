package com.rescuer.api.web.service;

import com.rescuer.api.entity.User;
import com.rescuer.api.entity.UserType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    /*@Mock
    private UserService userService;

    private UserDetailsService userDetailsService;

    @Before
    public void setup() {
        this.userDetailsService = new UserDetailsService(passwordEncoder,userService);
    }



    @Test
    public void authenticate() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        Optional<User> user = Optional.of(User.builder().isActive(true).userName("testuser1").userType(UserType.ADMIN).password("s".toCharArray()).build());
        when(userService.getUser(anyString())).
                thenReturn(user);
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        request.addHeader("Authorization", "Basic bmV4dExldmVsRGV2VGVzdDpXZWxjb21lQDEyMw==");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser1", "jsjdia#$diadadadd");

        // Act
        Authentication authenticatedUser = userDetailsService.authenticate(auth);

        // Assert
        assertThat(authenticatedUser).isNotNull();
    }
    @Test(expected = BadCredentialsException.class)
    public void badAuthenticate() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        Optional<User> user = Optional.of(User.builder().isActive(true).userName("testuser1").userType(UserType.ADMIN).password("s".toCharArray()).build());
        when(userService.getUser(anyString())).
                thenReturn(user);ya
        when(this.passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        request.addHeader("Authorization", "Basic bmV4dExldmVsRGV2VGVzdDpXZWxjb21lQDEyMw==");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        Authentication auth = new UsernamePasswordAuthenticationToken("testUser1", "jsjdia#$diadadadd");

        // Act
        userDetailsService.authenticate(auth);

    }*/
}