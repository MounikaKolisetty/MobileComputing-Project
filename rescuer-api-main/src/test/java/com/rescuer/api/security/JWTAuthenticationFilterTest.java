package com.rescuer.api.security;

import com.rescuer.api.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JWTAuthenticationFilterTest {
    private JWTAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private FilterChain filterChain;
    @Before
    public void setup(){
        this.jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager);
    }

    @Test
    public void generateToken() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = new User();
        user.setUserName("admin");
        user.setPassword("password");
        request.setContent(user.toString().getBytes());
        MockHttpServletResponse res = new MockHttpServletResponse();
        Authentication auth = new UsernamePasswordAuthenticationToken(user, "jsjdia#$diadadadd", new ArrayList<>(0));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        jwtAuthenticationFilter.successfulAuthentication(request,res,filterChain,auth);
        String token = res.getHeader("Authorization");


    }

}