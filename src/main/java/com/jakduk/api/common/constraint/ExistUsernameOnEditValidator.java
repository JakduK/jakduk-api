package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.restcontroller.vo.user.AuthUserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
public class ExistUsernameOnEditValidator implements ConstraintValidator<ExistUsernameOnEdit, String> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void initialize(ExistUsernameOnEdit constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value))
            return false;

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        if (ObjectUtils.isEmpty(authUserProfile))
            throw new ServiceException(ServiceError.NEED_TO_LOGIN);

        Optional<UserProfile> optUserProfile = userProfileRepository.findByNEIdAndUsername(authUserProfile.getId().trim(), value.trim());

        return ! optUserProfile.isPresent();

    }
}
