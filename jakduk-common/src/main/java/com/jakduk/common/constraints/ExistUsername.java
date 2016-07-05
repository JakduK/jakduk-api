package com.jakduk.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by pyohwan on 16. 7. 3.
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistUsernameValidator.class)
public @interface ExistUsername {

    String message() default "EXIST_USERNAME";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
