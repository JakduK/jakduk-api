package com.jakduk.core.common.constraints;


import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:28
 */

class ExistEmailCompatibilityValidator implements ConstraintValidator<ExistEmailCompatibility, Object> {

    private String userIdField;
    private String emailField;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistEmailCompatibility constraintAnnotation) {
        userIdField = constraintAnnotation.userId();
        emailField = constraintAnnotation.email();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            final String userId = BeanUtils.getProperty(value, userIdField);
            final String email = BeanUtils.getProperty(value, emailField);

            UserProfile existEmail = null;

            // Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
            if (Objects.nonNull(userId) && ! userId.isEmpty()) {
                existEmail = userService.findByNEIdAndEmail(userId, email);
            } else {
                existEmail = userService.findOneByEmail(email);
            }

            if (Objects.nonNull(existEmail)) {
                return false;
            }

        } catch (final Exception ignore) {
            // ignore
        }

        return true;
    }
}
