package com.jakduk.api.common;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTests {
	
	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
	
	@Test
	public void 스프링시큐리티_암호_인코딩() {
		String password = passwordEncoder.encode("1111");

		assertFalse(passwordEncoder.matches("1112", password));
		assertTrue(passwordEncoder.matches("1111", password));

		DelegatingPasswordEncoder newPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		newPasswordEncoder.setDefaultPasswordEncoderForMatches(new StandardPasswordEncoder());
		System.out.println(newPasswordEncoder.encode("1111"));
		assertTrue(newPasswordEncoder.matches("1111", password));
	}

}
