package jakduk;

import com.jakduk.configuration.AppConfig;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.service.CommonService;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@ActiveProfiles("local")
public class CommonTest {
	
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
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/reset_password"));
	}

	@Test
	public void environmentTest() {
		System.out.println(storageTempPath);
		System.out.println(environment.getProperty("mongo.db.name"));

	}
	
}
