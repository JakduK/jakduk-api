package com.jakduk.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by pyohwan on 16. 7. 3.
 */

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistEmailCompatibilityValidator.class)
public @interface ExistEmailCompatibility {

    String message() default "EXIST_EMAIL";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String userId();

    String email();
}
