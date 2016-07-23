package com.jakduk.common.constraints;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */
class ExistEmailOnEditValidator implements ConstraintValidator<ExistEmailOnEdit, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmailOnEdit constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (Objects.isNull(value))
            return false;

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile existEmail = userService.findByNEIdAndEmail(commonPrincipal.getId(), value.trim());

        return ! Objects.nonNull(existEmail);

    }
}
