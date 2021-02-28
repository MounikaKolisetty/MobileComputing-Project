package com.rescuer.api.web.error;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collection;

public class CollectionValidator implements Validator {

    private final Validator validator;

    public CollectionValidator(LocalValidatorFactoryBean validatorFactory) {
        this.validator = validatorFactory;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * Validate each element inside the supplied {@link Collection}.
     * <p>
     * The supplied errors instance is used to report the validation errors.
     *
     * @param target the collection that is to be validated
     * @param errors contextual state about the validation process
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void validate(Object target, Errors errors) {
        Collection collection = (Collection) target;
        for (Object object : collection) {
            ValidationUtils.invokeValidator(validator, object, errors);
        }
    }
}