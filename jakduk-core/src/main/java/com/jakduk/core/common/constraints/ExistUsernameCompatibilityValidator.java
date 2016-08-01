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
 * 16. 7. 3 오후 9:41
 */

class ExistUsernameCompatibilityValidator implements ConstraintValidator<ExistUsernameCompatibility, Object> {

    private String userIdField;
    private String usernameField;

    @Autowired
    private UserService userService;

    @Override
    public void initialize(ExistUsernameCompatibility constraintAnnotation) {
        userIdField = constraintAnnotation.userId();
        usernameField = constraintAnnotation.username();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        try {
            final String userId = BeanUtils.getProperty(value, userIdField);
            final String username = BeanUtils.getProperty(value, usernameField);

            UserProfile existUsername = null;

            // Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
            if (Objects.nonNull(userId) && ! userId.isEmpty()) {
                existUsername = userService.findByNEIdAndUsername(userId, username);
            } else {
                existUsername = userService.findOneByUsername(username);
            }

            if (Objects.nonNull(existUsername)) {
                return false;
            }

        } catch (final Exception ignore) {
            // ignore
        }

        return true;
    }
}
