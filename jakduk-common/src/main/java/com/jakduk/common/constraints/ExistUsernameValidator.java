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
public class ExistUsernameValidator implements ConstraintValidator<ExistUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        UserProfile existUsername = userService.findOneByUsername(value);
        if (Objects.nonNull(existUsername)) {
            return false;
        }

        return true;
    }
}
