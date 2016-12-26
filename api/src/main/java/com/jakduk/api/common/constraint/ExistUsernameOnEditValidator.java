package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.configuration.authentication.user.CommonPrincipal;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

        if (StringUtils.isEmpty(value))
            return false;

        CommonPrincipal commonPrincipal = UserUtils.getCommonPrincipal();

        if (ObjectUtils.isEmpty(commonPrincipal))
            throw new ServiceException(ServiceError.NEED_TO_LOGIN);

        UserProfile userProfile = userService.findByNEIdAndUsername(commonPrincipal.getId().trim(), value.trim());

        return ObjectUtils.isEmpty(userProfile);

    }
}
