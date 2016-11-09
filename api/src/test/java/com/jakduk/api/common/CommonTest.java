package com.jakduk.api.common;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.service.CommonService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

public class CommonTest extends AbstractSpringTest {
	
	@Autowired
	CommonService commonService;

	@Value("${storage.temp.path}")
	private String storageTempPath;

	@Autowired
	private Environment environment;

	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
	
	@Test
	public void environmentTest() {
		System.out.println(storageTempPath);
		System.out.println(environment.getProperty("mongo.db.name"));
	}

	@Test
	public void 스프링시큐리티_암호_인코딩() {
		//User user = userRepository.findByUsername("test01");
		//String pwd = user.getPassword();

		String password = passwordEncoder.encode("1111");

		Assert.assertFalse(passwordEncoder.matches("1112", password));
		Assert.assertTrue(passwordEncoder.matches("1111", password));
	}
	
}
