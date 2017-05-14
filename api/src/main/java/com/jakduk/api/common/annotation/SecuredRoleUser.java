package com.jakduk.api.common.annotation;

import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

/**
 * Created by pyohwanjang on 2017. 5. 14..
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Secured({"ROLE_USER_01", "ROLE_USER_02", "ROLE_USER_03"})
public @interface SecuredRoleUser {
}
