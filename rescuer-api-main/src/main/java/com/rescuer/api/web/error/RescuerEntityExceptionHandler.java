package com.rescuer.api.web.error;

import com.rescuer.api.util.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class RescuerEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final LocalValidatorFactoryBean validator;

    @Autowired
    public RescuerEntityExceptionHandler(final MessageSource messageSource, final LocalValidatorFactoryBean validator) {
        this.messageSource = messageSource;
        this.validator = validator;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", HttpStatus.BAD_REQUEST);
        ValidationError errors = ValidationError.generateValidationErrorResponse(ex.getBindingResult().getAllErrors(),
                messageSource);
        body.put("messages", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof Collection) {
            binder.addValidators(new CollectionValidator(validator));
        }
    }
}
