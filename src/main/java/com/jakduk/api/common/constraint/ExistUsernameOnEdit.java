package com.jakduk.api.common.constraint;

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

@Constraint(validatedBy = ExistUsernameOnEditValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUsernameOnEdit {

    String message() default "{validation.msg.username.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
