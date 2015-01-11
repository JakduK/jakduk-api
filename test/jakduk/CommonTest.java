package jakduk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.model.web.BoardPageInfo;
import com.jakduk.service.CommonService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CommonTest {
	
	@Autowired
	CommonService commonService;
	
	@Test
	public void urlTest() {
		
		System.out.println(commonService.isRedirectUrl("http://localhost:8080/jakduk/about/intro"));
		System.out.println(commonService.isRedirectUrl("http://localhost:8080/jakduk/login"));
		System.out.println(commonService.isRedirectUrl("http://localhost:8080/jakduk/board/free/write"));
	}
	
	@Test
	public void getCountPages() {
		
		BoardPageInfo boardPageInfo = commonService.getCountPages((long)11, (long)50, 5);
		
		System.out.println("getCountPages=" + boardPageInfo);
	}

}
