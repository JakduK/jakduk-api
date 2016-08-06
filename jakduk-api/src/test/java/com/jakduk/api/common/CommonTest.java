package com.jakduk.api.common;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.service.CommonService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

public class CommonTest extends AbstractSpringTest {
	
	@Autowired
	CommonService commonService;

	@Value("${storage.temp.path}")
	private String storageTempPath;

	@Autowired
	private Environment environment;
	
	@Test
	public void environmentTest() {
		System.out.println(storageTempPath);
		System.out.println(environment.getProperty("mongo.db.name"));

	}
	
}
