package com.jakduk.core;

import org.springframework.test.context.ActiveProfilesResolver;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2017. 2. 7.
 */
public class CoreTestActiveProfileResolver implements ActiveProfilesResolver {

	@Override public String[] resolve(Class<?> testClass) {

		String profile = System.getProperty("spring.profiles.active");

		if (profile == null || profile.isEmpty()) {
			profile = "core-default";
		}

		return new String[] { profile };
	}
}
