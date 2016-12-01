package com.jakduk.api.common.constraint;

import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */

public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value))
            return false;

        UserProfile existEmail = userService.findOneByEmail(value);

        return ObjectUtils.isEmpty(existEmail);
    }
}
