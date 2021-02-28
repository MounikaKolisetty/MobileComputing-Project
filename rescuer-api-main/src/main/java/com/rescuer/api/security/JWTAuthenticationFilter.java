package com.rescuer.api.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rescuer.api.dtos.responseDtos.TokenDto;
import com.rescuer.api.entity.User;
import com.rescuer.api.web.util.JWTHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.rescuer.api.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TOKEN_PREFIX = "Bearer ";
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            com.rescuer.api.entity.User creds = new ObjectMapper()
                    .readValue(req.getInputStream(), com.rescuer.api.entity.User.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    creds,
                    "", new ArrayList<>());
            this.setDetails(req, authRequest);
            return authenticationManager.authenticate(
                    authRequest
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        User user = (com.rescuer.api.entity.User) auth.getPrincipal();
        String tokenId = JWTHelper
                .getJWTtoken(user, TOKEN_EXPIRATION_TIME, SECRET);
        String refreshToken = JWTHelper
                .getJWTtoken(user, EXPIRATION_TIME, SECRET);
        TokenDto tokenDto = TokenDto.builder().tokenId(tokenId).refreshToken(refreshToken).build();
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write(tokenDto.toString());
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + tokenId);
        res.addHeader(USER_TYPE_HEADER_STRING, user.getUserType().name());
    }
}
