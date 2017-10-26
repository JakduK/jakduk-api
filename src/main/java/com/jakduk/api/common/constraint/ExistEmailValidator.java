package com.jakduk.api.common.constraint;

import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;
import com.jakduk.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.Optional;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */

@Component
public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void initialize(ExistEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Objects.requireNonNull(value);

        Optional<UserProfile> optUserProfile = userProfileRepository.findOneByEmail(value.trim());

        return ! optUserProfile.isPresent();
    }

}
