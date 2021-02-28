package com.rescuer.api.web.util;

import com.auth0.jwt.JWT;
import com.rescuer.api.entity.User;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTHelper {

    public static String getJWTtoken(User user, long exp, String secret) {
        return JWT.create()
                .withSubject((user).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + exp))
                .sign(HMAC512(secret.getBytes()));
    }
}
