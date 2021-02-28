package com.rescuer.api.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final long TOKEN_EXPIRATION_TIME = 4_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String USER_TYPE_HEADER_STRING = "user_type";
    public static final String SIGN_UP_URL = "/user/sign-up";
}
