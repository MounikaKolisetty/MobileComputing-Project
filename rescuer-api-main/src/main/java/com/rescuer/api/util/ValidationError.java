package com.rescuer.api.util;

import org.springframework.context.MessageSource;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ValidationError {

    private final Map<String, String> details;

    private ValidationError(String message, List<ObjectError> errorList) {
        details = new HashMap<>(errorList.size());
        errorList.stream().forEach(entry -> {
            details.put("field", entry.getCodes()[0].split("\\.")[2]);
            details.put("message", entry.getDefaultMessage());
        });
    }
    
    private ValidationError(List<ObjectError> errorList) {
        details = new HashMap<>(errorList.size());
        errorList.stream().forEach(entry -> {
            details.put("field", entry.getObjectName());
            details.put("message", entry.getDefaultMessage());
        });
    }

    public static ValidationError generateValidationErrorResponse(List<ObjectError> errors,
            MessageSource messageSource) {
        return new ValidationError(messageSource.getMessage("validations.message", null, null), errors);
    }
    
    public static ValidationError generateValidationErrorResponse(List<ObjectError> errors) {
        return new ValidationError(errors);
    }

    public static ValidationError generateValidationErrorResponse(String fieldName, RuntimeException exception) {
        return new ValidationError(Arrays.asList(new ObjectError(fieldName, exception.getMessage())));
    }

    public Map<String, String> getDetails() {
        return details;
    }

}