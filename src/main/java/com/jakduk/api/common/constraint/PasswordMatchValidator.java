package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import com.jakduk.api.restcontroller.vo.user.SessionUser;
import com.jakduk.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, String> {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isBlank(value))
            return false;

        SessionUser sessionUser = AuthUtils.getAuthUserProfile();

        if (! AuthUtils.isJakdukUser())
            return true;

        UserOnPasswordUpdate user = userService.findUserOnPasswordUpdateById(sessionUser.getId());
        String oldPassword = user.getPassword();

        return encoder.matches(value, oldPassword);

    }
}
