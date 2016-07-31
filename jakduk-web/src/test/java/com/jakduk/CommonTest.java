package com.jakduk;

import com.jakduk.service.CommonService;
import com.jakduk.util.AbstractSpringTest;
import org.junit.Assert;
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
	public void urlTest() {

		Assert.assertTrue(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/about/intro"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/login"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/board/free/write"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/password/reset"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/password/find?lang=ko_kr"));
	}

	@Test
	public void environmentTest() {
		System.out.println(storageTempPath);
		System.out.println(environment.getProperty("mongo.db.name"));

	}
	
}
