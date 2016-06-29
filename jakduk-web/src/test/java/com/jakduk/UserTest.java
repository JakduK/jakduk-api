package com.jakduk;

import com.jakduk.common.CommonConst;
import com.jakduk.common.CommonRole;
import com.jakduk.dao.JakdukDAO;
import com.jakduk.model.db.User;
import com.jakduk.model.etc.SupporterCount;
import com.jakduk.model.simple.SocialUserOnAuthentication;
import com.jakduk.model.simple.UserOnAuthentication;
import com.jakduk.model.simple.UserProfile;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.user.UserProfileRepository;
import com.jakduk.repository.user.UserRepository;
import com.jakduk.util.AbstractSpringTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookAdapter;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 26.
 * @desc     :
 */

public class UserTest extends AbstractSpringTest {

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

	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Autowired
	private Environment environment;

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

		UserProfile userProfile02 = userProfileRepository.findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE.DAUM, "1lnkE");
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

		FacebookTemplate facebookTemplate = new FacebookTemplate("EAALwXK7RDAIBABRLhRHZB8DV9GXKSLfSlvZBGkjXbVziSQRuPTqpc2eAvZBXcd9XM130euKDAF9wiZCCBroeZCT3PUpedn9U8WzZAY5q4rKCyQSRUkcGtON0aS95r46s1a2i4OTYMXDE5F8yEZBiw3X20VWXZBz33VjjMMG3hbNbtguVYjlZBJWXq");

		org.springframework.social.facebook.api.User user = facebookTemplate.userOperations().getUserProfile();

		System.out.println("userProfile=" + user.getEmail());
		System.out.println("userProfile=" + user.getId());
		System.out.println("userProfile=" + user.getName());


		Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo("facebook", new HashSet<String>(Arrays.asList("917745831573018")));

		System.out.println("phjang=" + userIds);

		UserOnAuthentication user1 = userRepository.findAuthUserByEmail(email);
		System.out.println("user1=" + user1);

		SocialUserOnAuthentication user3 = userRepository.findSocialUserByEmail(email);
		System.out.println("user3=" + user3);

		User user2 = userRepository.findOneByEmail(email);
		System.out.println("user2=" + user2);
	}

	@Test
	public void 페이스북_OAUTH2() {
		String accessToken = "EAALwXK7RDAIBABRLhRHZB8DV9GXKSLfSlvZBGkjXbVziSQRuPTqpc2eAvZBXcd9XM130euKDAF9wiZCCBroeZCT3PUpedn9U8WzZAY5q4rKCyQSRUkcGtON0aS95r46s1a2i4OTYMXDE5F8yEZBiw3X20VWXZBz33VjjMMG3hbNbtguVYjlZBJWXq";
		FacebookConnectionFactory connectionFactory = new FacebookConnectionFactory(environment.getProperty("facebook.app.id"), environment.getProperty("facebook.app.secret"));

		AccessGrant accessGrant = new AccessGrant(accessToken);
		Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);

		ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection);

		System.out.println("profile=" + connection.getDisplayName());
	}

}
