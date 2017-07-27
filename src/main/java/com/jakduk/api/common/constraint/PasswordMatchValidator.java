package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import com.jakduk.api.vo.user.AuthUserProfile;
import com.jakduk.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, String> {

    @Autowired
    private StandardPasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (Objects.isNull(value))
            return false;

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        if (! AuthUtils.isJakdukUser())
            return true;

        UserOnPasswordUpdate user = userService.findUserOnPasswordUpdateById(authUserProfile.getId());
        String oldPassword = user.getPassword();

        return encoder.matches(value, oldPassword);

    }
}
