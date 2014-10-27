package jakduk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.UserRepository;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 26.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserTest {
	
	@Autowired
	UserRepository userRepository;

	@Test
	public void test01() {
		
		UserProfile user = userRepository.userFindByNEIdAndUsername("544dd2a13d9648d912a339c7", "test05");		
		System.out.println("aaaa=" + user);
	}

}
