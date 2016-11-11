package com.jakduk.api.common.constraints;

import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
class ExistUsernameValidator implements ConstraintValidator<ExistUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsername constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        UserProfile existUsername = userService.findOneByUsername(value);

        return ! Objects.nonNull(existUsername);

    }
}