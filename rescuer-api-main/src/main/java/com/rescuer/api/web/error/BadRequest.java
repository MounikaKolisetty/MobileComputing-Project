package com.rescuer.api.web.error;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
