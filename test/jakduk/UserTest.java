package jakduk;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonRole;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.SupporterCount;
import com.jakduk.model.simple.OAuthProfile;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.FootballClubRepository;
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
	private ApplicationContext applicationContext;
	
	@Autowired
	private StandardPasswordEncoder encoder;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FootballClubRepository footballClubRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private JakdukDAO jakdukDAO;
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void test01() {
		
		UserProfile user = userRepository.userFindByNEIdAndUsername("544dd2a13d9648d912a339c7", "test05");		
		System.out.println("UserProfile=" + user);
	}
	
	@Test
	public void test02() {
/*						
		User user = userRepository.findByUsername("test01");		
		String pwd = user.getPassword();
		System.out.println("user =" + user);
		System.out.println("user pwd=" + pwd);
		
		String result = encoder.encode("1111");
//		System.out.println("result=" + result);
		
		System.out.println(encoder.matches("1112", result));
		assertTrue(encoder.matches("1111", result));
		*/
	}
	
	@Test
	public void test03() {
		OAuthProfile user = userRepository.userFindByNEOauthIdAndUsername("100000128296954", "Pyohwan Jang");		
		System.out.println("OAuthProfile=" + user);
		
		UserProfile userProfile = userRepository.userFindByNEIdAndUsername("545cbdfb3d9627e574001668", "test07");
		System.out.println("userProfile=" + userProfile);
	}
	
	@Test
	public void roleTest01() {
		
		String roleName = CommonRole.getRoleName(10);
		
		System.out.println("roleName=" + roleName);
		System.out.println("roleNumber=" + CommonRole.getRoleNumber(roleName));
			
	}
	
	@Test
	public void getSupportFCCount() {
		List<SupporterCount> users = jakdukDAO.getSupportFCCount("ko");
		System.out.println("getSupportFCCount=" + users);
		
	}

}
