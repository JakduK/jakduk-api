package com.jakduk.core.common.constraints;

import com.jakduk.core.authentication.common.CommonPrincipal;
import com.jakduk.core.model.simple.UserOnPasswordUpdate;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.UserService;
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

    @Autowired
    private CommonService commonService;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (Objects.isNull(value))
            return false;

        CommonPrincipal commonPrincipal = userService.getCommonPrincipal();

        if (! commonService.isJakdukUser())
            return true;

        UserOnPasswordUpdate user = userService.findUserOnPasswordUpdateById(commonPrincipal.getId());
        String oldPassword = user.getPassword();

        return encoder.matches(value, oldPassword);

    }
}
