package com.jakduk.api.common.constraints;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
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
class ExistUsernameOnEditValidator implements ConstraintValidator<ExistUsernameOnEdit, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsernameOnEdit constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (Objects.isNull(value))
            return false;

        CommonPrincipal commonPrincipal = UserUtils.getCommonPrincipal();

        UserProfile existUsername = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), value.trim());

        return ! Objects.nonNull(existUsername);

    }
}