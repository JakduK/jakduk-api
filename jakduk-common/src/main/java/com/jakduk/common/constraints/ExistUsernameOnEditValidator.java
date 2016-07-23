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
 * 16. 7. 3 오후 9:40
 */
public class ExistUsernameOnEditValidator implements ConstraintValidator<ExistUsernameOnEdit, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsernameOnEdit constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        UserProfile existUsername = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), value.trim());

        return ! Objects.nonNull(existUsername);

    }
}
