package com.jakduk.core.common.constraints;

import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */
class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        UserProfile existEmail = userService.findOneByEmail(value);

        return ! Objects.nonNull(existEmail);

    }
}
