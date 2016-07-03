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

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistUsernameCompatibilityValidator.class)
public @interface ExistUsernameCompatibility {

    String message() default "EXIST_USERNAME";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String userId();

    String username();
}
