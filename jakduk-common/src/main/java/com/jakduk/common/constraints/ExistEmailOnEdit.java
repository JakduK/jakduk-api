package com.jakduk.common.constraints;

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

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistEmailOnEditValidator.class)
public @interface ExistEmailOnEdit {

    String message() default "email_Exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
