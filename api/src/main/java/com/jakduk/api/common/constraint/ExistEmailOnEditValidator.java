package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.common.vo.AuthUserProfile;
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

        AuthUserProfile authUserProfile = UserUtils.getAuthUserProfile();

        if (ObjectUtils.isEmpty(authUserProfile))
            return false;

        UserProfile userProfile = userService.findByNEIdAndEmail(authUserProfile.getId(), value.trim());

        return ObjectUtils.isEmpty(userProfile);

    }
}
