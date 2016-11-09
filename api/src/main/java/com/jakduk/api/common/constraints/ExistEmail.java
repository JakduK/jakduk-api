package com.jakduk.api.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 이메일 중복 검사
 *
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistEmailValidator.class)
public @interface ExistEmail {

    String message() default "{validation.msg.email.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
