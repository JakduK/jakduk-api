package com.jakduk.common.constraints;

import com.jakduk.model.simple.UserProfile;
import com.jakduk.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Created by pyohwan on 16. 7. 3.
 */

@Slf4j
public class ExistEmailCompatibilityValidator implements ConstraintValidator<ExistEmailCompatibility, Object> {

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
            if (Objects.nonNull(userId) && userId.isEmpty() == false) {
                existEmail = userService.findByNEIdAndEmail(userId, email);
                log.debug("existEmail=" + existEmail);
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
