package com.jakduk.api.common.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pyohwan
 * 16. 7. 3 오후 9:41
 */

@Constraint(validatedBy = ExistUsernameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUsername {

    String message() default "{com.jakduk.api.common.constraint.ExistEmail.description}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
