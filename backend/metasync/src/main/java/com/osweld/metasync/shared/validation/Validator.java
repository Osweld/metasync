package com.osweld.metasync.shared.validation;


import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

public class Validator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final jakarta.validation.Validator validator = factory.getValidator();

    public static <T> void validate(T subject){
        Set<ConstraintViolation<T>> violations = validator.validate(subject);
        if(!violations.isEmpty()){
            throw new ConstraintViolationException(violations);
        }
    }

}
