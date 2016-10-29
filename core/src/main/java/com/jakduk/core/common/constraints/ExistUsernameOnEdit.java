package com.jakduk.core.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 회원 프로필 편집 시 별명 중복 검사
 *
 * @author pyohwan
 * 16. 7. 3 오후 9:41
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistUsernameOnEditValidator.class)
public @interface ExistUsernameOnEdit {

    String message() default "username_Exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
