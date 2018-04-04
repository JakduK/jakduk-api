package com.jakduk.api;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJakdukUserSecurityContextFactory.class)
public @interface WithMockJakdukUser {

    String username() default "test07@test.com";
    String name() default "test07";

}
