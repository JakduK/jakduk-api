package com.jakduk.api.user;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.common.CommonConst;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.dao.JakdukDAO;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.etc.SupporterCount;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.repository.FootballClubRepository;
import com.jakduk.core.repository.user.UserProfileRepository;
import com.jakduk.core.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;


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

	@Before
	public void setUp() {
	}

	/**
	 * prod 에선 실패.
	 */
	@Ignore
	@Test
	public void 회원_정보_편집() {
		String id = "5703d2e2e4b07a2fcef75ec3";
		UserProfile userProfile = userProfileRepository.findOne(id);
		Optional<User> user = userRepository.findOneById(id);

		System.out.println("UserProfile=" + userProfile);
		System.out.println("user=" + user.get());
	}

	@Test
	public void test03() {
		//OAuthProfile user = userRepository.userFindByNEOauthIdAndUsername("100000128296954", "Pyohwan Jang");
		//System.out.println("OAuthProfile=" + user);
		
		UserProfile userProfile01 = userProfileRepository.findByNEIdAndUsername("545cbdfb3d9627e574001668", "test07");
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
	public void ID를제외하고_이메일로_회원찾기() {
		UserProfile userProfile = userProfileRepository.findByNEIdAndEmail("54abdc143d96bf2c2b48adbb", "test05@test.com");
		System.out.println("userProfile=" + userProfile);

		UserProfile userProfile1 = userProfileRepository.findByNEIdAndUsername("54abdc143d96bf2c2b48adbb", "생글이_진주FC");
		System.out.println("userProfile1=" + userProfile1);

	}

}
