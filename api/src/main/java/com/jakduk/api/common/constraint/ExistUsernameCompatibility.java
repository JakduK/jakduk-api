package com.jakduk.api.common.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Social 기반으로 가입시 이메일 중복 검사.
 * Version 0.6.0 이전, User 데이터의 하위 호환성 유지를 위함이다. https://github.com/Pyohwan/JakduK/issues/53
 * @author pyohwan
 * 16. 7. 3 오후 9:32
 */

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistUsernameCompatibilityValidator.class)
public @interface ExistUsernameCompatibility {

    String message() default "{validation.msg.username.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String userId();

    String username();
}
