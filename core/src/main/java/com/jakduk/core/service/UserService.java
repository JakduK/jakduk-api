package com.jakduk.core.service;


import com.jakduk.core.common.CommonConst;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.simple.UserOnPasswordUpdate;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.repository.FootballClubRepository;
import com.jakduk.core.repository.user.UserProfileRepository;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FootballClubRepository footballClubRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	public User findById(String id) {
		return userRepository.findOne(id);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public UserProfile findUserProfileById(String id) {
		return userProfileRepository.findOne(id);
	}

	// email과 일치하는 회원 찾기.
	public UserProfile findOneByEmail(String email) {
		return userProfileRepository.findOneByEmail(email);
	}

	// username과 일치하는 회원 찾기.
	public UserProfile findOneByUsername(String username) {
		return userProfileRepository.findOneByUsername(username);
	}

	// 해당 ID를 제외하고 username과 일치하는 회원 찾기.
	public UserProfile findByNEIdAndUsername(String id, String username) {
		return userProfileRepository.findByNEIdAndUsername(id, username);
	};

	// 해당 ID를 제외하고 email과 일치하는 회원 찾기.
	public UserProfile findByNEIdAndEmail(String id, String email) {
		return userProfileRepository.findByNEIdAndEmail(id, email);
	}

	// SNS 계정으로 가입한 회원 찾기.
	public UserProfile findUserProfileByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId) {
		return userProfileRepository.findOneByProviderIdAndProviderUserId(providerId, providerUserId);
	}

	// SNS 계정으로 가입한 회원 찾기(로그인).
	public User findOneByProviderIdAndProviderUserId(CommonConst.ACCOUNT_TYPE providerId, String providerUserId) {
		return userRepository.findOneByProviderIdAndProviderUserId(providerId, providerUserId);
	}

	/**
	 * 비밀번호 변경을 위한 이메일 기반 회원 가져오기
	 *
	 * @param id userID
	 * @return UserOnPasswordUpdate
     */
	public UserOnPasswordUpdate findUserOnPasswordUpdateById(String id){

		return userRepository.findUserOnPasswordUpdateById(id);
	}

	// 회원 정보 저장.
	public void save(User user) {
		userRepository.save(user);
	}

	public User addJakdukUser(String email, String username, String password, String footballClub, String about) {
		User user = User.builder()
				.email(email.trim())
				.username(username.trim())
				.password(password)
				.providerId(CommonConst.ACCOUNT_TYPE.JAKDUK)
				.roles(Collections.singletonList(CommonRole.ROLE_NUMBER_USER_01))
				.build();

		if (StringUtils.isNotEmpty(footballClub)) {
			FootballClub supportFC = footballClubRepository.findOneById(footballClub)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(supportFC);
		}

		if (StringUtils.isNotEmpty(about))
			user.setAbout(about.trim());

		User returnUser = userRepository.save(user);

		log.debug("JakduK user created. user=" + returnUser);

		return returnUser;
	}

	public User addSocialUser(String email, String username, CommonConst.ACCOUNT_TYPE providerId, String providerUserId, String footballClub, String about) {
		User user = User.builder()
				.email(email.trim())
				.username(username.trim())
				.providerId(providerId)
				.providerUserId(providerUserId)
				.roles(Collections.singletonList(CommonRole.ROLE_NUMBER_USER_01))
				.build();

		if (StringUtils.isNotEmpty(footballClub)) {
			FootballClub supportFC = footballClubRepository.findOneById(footballClub)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(supportFC);
		}

		if (StringUtils.isNotEmpty(about))
			user.setAbout(about.trim());

		User returnUser = userRepository.save(user);

		log.debug("social user created. user=" + returnUser);

		return returnUser;
	}

	public void existEmail(String email) {
		Optional<User> oUser = userRepository.findOneByEmail(email);

		if (oUser.isPresent())
			throw new ServiceException(ServiceError.ALREADY_EXIST_EMAIL);
	}
	
	public void existUsername(String username) {
		Optional<User> oUser = userRepository.findOneByUsername(username);

		if (oUser.isPresent())
			throw new ServiceException(ServiceError.ALREADY_EXIST_USERNAME);
	}

	/**
	 * 이메일 기반 회원의 비밀번호 변경.
	 *
	 * @param newPassword 새 비밀번호
     */
	public void updateUserPassword(String userId, String newPassword) {
		User user = userRepository.findOneById(userId).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));
		user.setPassword(newPassword);
		
		this.save(user);

		log.info("jakduk user password changed. email=" + user.getEmail() + ", username=" + user.getUsername());
	}

	public void userPasswordUpdateByEmail(String email, String password) {
		User user = userRepository.findOneByEmail(email).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));
		user.setPassword(password);

		this.save(user);

		log.info("jakduk user password changed. id=" + user.getId() + ", username=" + user.getUsername());
	}

}
