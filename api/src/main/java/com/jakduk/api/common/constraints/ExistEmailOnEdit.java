package com.jakduk.api.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 회원 프로필 편집 시 이메일 중복 검사
 *
 * @author pyohwan
 * 16. 7. 3 오후 9:30
 */

@Constraint(validatedBy = ExistEmailOnEditValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistEmailOnEdit {

    String message() default "{validation.msg.email.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
