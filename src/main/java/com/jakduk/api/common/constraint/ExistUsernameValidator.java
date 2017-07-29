package com.jakduk.api.common.constraint;

import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
public class ExistUsernameValidator implements ConstraintValidator<ExistUsername, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value))
            return false;

        UserProfile existUsername = userService.findOneByUsername(value);

        return ObjectUtils.isEmpty(existUsername);

    }
}
