package jakduk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.service.CommonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CommonTest {
	
	@Autowired
	CommonService commonService;
	
	@Test
	public void urlTest() {

		Assert.assertTrue(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/about/intro"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/login"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/board/free/write"));
		Assert.assertFalse(commonService.isRedirectUrl("http://localhost:8080/jakduk-web/reset_password"));
	}
	
}
