package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.vo.user.AuthUserProfile;
import com.jakduk.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */
public class ExistEmailOnEditValidator implements ConstraintValidator<ExistEmailOnEdit, String> {

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmailOnEdit constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value))
            return false;

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        if (ObjectUtils.isEmpty(authUserProfile))
            return false;

        UserProfile userProfile = userService.findByNEIdAndEmail(authUserProfile.getId(), value.trim());

        return ObjectUtils.isEmpty(userProfile);

    }
}
