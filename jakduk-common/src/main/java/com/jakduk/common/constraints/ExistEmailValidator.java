package com.jakduk.common.constraints;

import com.jakduk.model.simple.UserProfile;
import com.jakduk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 7. 3.
 */
public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        UserProfile existEmail = userService.findOneByEmail(value);
        if (Objects.nonNull(existEmail)) {
            return false;
        }

        return true;
    }
}
