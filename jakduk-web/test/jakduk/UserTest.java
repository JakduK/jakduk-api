package jakduk;

import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.configuration.AppConfig;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.User;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.model.simple.OAuthProfile;
import com.jakduk.model.simple.SocialUserOnAuthentication;
import com.jakduk.model.simple.UserOnAuthentication;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.user.UserProfileRepository;
import com.jakduk.repository.user.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 26.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserProfileRepository userProfileRepository;
	
	@Autowired
	FootballClubRepository footballClubRepository;

	@Autowired
	private JakdukDAO jakdukDAO;

	@Autowired
	private StandardPasswordEncoder encoder;
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void test01() {
		UserProfile user = userRepository.findByNEIdAndUsername("544dd2a13d9648d912a339c7", "test05");
		System.out.println("UserProfile=" + user);
	}

	@Test
	public void 회원_정보_편집() {
		String id = "5703d2e2e4b07a2fcef75ec3";
		UserProfile userProfile = userProfileRepository.findOne(id);
		User user = userRepository.findById(id);

		System.out.println("UserProfile=" + userProfile);
		System.out.println("user=" + user);
	}
	
	@Test
	public void 스프링시큐리티_암호_인코딩() {
		//User user = userRepository.findByUsername("test01");
		//String pwd = user.getPassword();

		String password = encoder.encode("1111");

		Assert.assertFalse(encoder.matches("1112", password));
		Assert.assertTrue(encoder.matches("1111", password));
	}

	@Test
	public void test03() {
		//OAuthProfile user = userRepository.userFindByNEOauthIdAndUsername("100000128296954", "Pyohwan Jang");
		//System.out.println("OAuthProfile=" + user);
		
		UserProfile userProfile01 = userRepository.findByNEIdAndUsername("545cbdfb3d9627e574001668", "test07");
		System.out.println("userProfile01=" + userProfile01);

		UserProfile userProfile02 = userRepository.findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE.DAUM, "1lnkE");
		System.out.println("userProfile02=" + userProfile02);
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

	@Test
	public void getSocialUser() {

		String email = "phjang1983@daum.net";

		UserOnAuthentication user1 = userRepository.userFindByEmail(email);
		System.out.println("user1=" + user1);

		SocialUserOnAuthentication user3 = userRepository.findSocialUserByEmail(email);
		System.out.println("user3=" + user3);

		User user2 = userRepository.findOneByEmail(email);
		System.out.println("user2=" + user2);
	}

}
