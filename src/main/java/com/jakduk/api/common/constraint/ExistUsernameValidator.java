package com.jakduk.api.common.constraint;

import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.restcontroller.vo.user.AuthUserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:40
 */
public class ExistUsernameValidator implements ConstraintValidator<ExistUsername, String> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void initialize(ExistUsername constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value))
            return false;

        AuthUserProfile authUserProfile = AuthUtils.getAuthUserProfile();

        if (Objects.isNull(authUserProfile)) {
            Optional<UserProfile> optUserProfile = userProfileRepository.findOneByUsername(value.trim());

            return ! optUserProfile.isPresent();
        } else {
            Optional<UserProfile> optUserProfile = userProfileRepository.findByNEIdAndUsername(authUserProfile.getId(), value.trim());

            return ! optUserProfile.isPresent();
        }
    }

}
