package com.jakduk.api.user;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.dao.JakdukDAO;
import com.jakduk.api.model.aggregate.SupporterCount;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;
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

public class UserTest extends ApiApplicationTests {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserProfileRepository userProfileRepository;
	
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
	public void getSupportFCCount() {
		List<SupporterCount> users = jakdukDAO.getSupportFCCount("ko");
		System.out.println("getSupportFCCount=" + users);
		
	}

}
