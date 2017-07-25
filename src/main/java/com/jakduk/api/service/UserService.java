package com.jakduk.api.service;


import com.jakduk.api.common.CommonRole;
import com.jakduk.api.common.CoreConst;
import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.FileUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.footballclub.FootballClubRepository;
import com.jakduk.api.repository.user.UserPictureRepository;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;
import com.jakduk.api.vo.user.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

	@Resource private JakdukProperties.Storage storageProperties;
	@Resource private AuthUtils authUtils;

	@Autowired private UserRepository userRepository;
	@Autowired private FootballClubRepository footballClubRepository;
	@Autowired private UserProfileRepository userProfileRepository;
	@Autowired private UserPictureRepository userPictureRepository;

	public Optional<User> findOneByProviderIdAndProviderUserId(CoreConst.ACCOUNT_TYPE providerId, String providerUserId) {
		return userRepository.findOneByProviderIdAndProviderUserId(providerId, providerUserId);
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

	/**
	 * 비밀번호 변경을 위한 이메일 기반 회원 가져오기
	 *
	 * @param id userID
	 * @return UserOnPasswordUpdate
     */
	public UserOnPasswordUpdate findUserOnPasswordUpdateById(String id){

		return userRepository.findUserOnPasswordUpdateById(id);
	}

	/**
	 * SNS 회원의 하위 호환 검사
	 *
	 * @param snsEmail Provider(페이스북, 다음)에서 가져온 email
	 * @param user DB에서 가져온 회원
	 */
	public void checkBackwardCompatibilityOfSnsUser(String snsEmail, User user) {

		// User DB 와 SNS Profile 모두에 email이 없을 경우에는 신규 가입으로 진행한다.
		// SNS 가입시 이메일 제공 동의를 안해서 그렇다.
		if (StringUtils.isBlank(user.getEmail()) && StringUtils.isBlank(snsEmail)) {
			user.setEmail(ApiUtils.generateTemporaryEmail());
			userRepository.save(user);

			log.info("user({},{}) temporary email:{} has been entered.", user.getId(), user.getUsername(), user.getEmail());
		}
		// 과거 SNS 가입 회원들은 email이 없는 경우가 있음. 이메일을 DB에 저장
		else if (StringUtils.isBlank(user.getEmail()) && StringUtils.isNotBlank(snsEmail)) {
			user.setEmail(snsEmail);
			userRepository.save(user);

			log.info("user({},{}) email:{} has been entered.", user.getId(), user.getUsername(), user.getEmail());
		}
	}

	// 회원 정보 저장.
	public void save(User user) {
		userRepository.save(user);
	}

	public User addJakdukUser(String email, String username, String password, String footballClub, String about, String userPictureId) {

		UserPicture userPicture = null;

		User user = User.builder()
				.email(email.trim())
				.username(username.trim())
				.password(password)
				.providerId(CoreConst.ACCOUNT_TYPE.JAKDUK)
				.roles(Collections.singletonList(CommonRole.ROLE_NUMBER_USER_01))
				.lastLogged(LocalDateTime.now())
				.build();

		if (StringUtils.isNotBlank(footballClub)) {
			FootballClub supportFC = footballClubRepository.findOneById(footballClub)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(supportFC);
		}

		if (StringUtils.isNotBlank(about))
			user.setAbout(about.trim());

		if (StringUtils.isNotBlank(userPictureId)) {
			userPicture = userPictureRepository.findOneById(userPictureId)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));

			user.setUserPicture(userPicture);
		}

		userRepository.save(user);

		if (! ObjectUtils.isEmpty(userPicture)) {
			userPicture.setUser(user);
			userPicture.setStatus(CoreConst.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.info("JakduK user created. email:{} username:{}", user.getEmail(), user.getUsername());

		return user;
	}

	/**
	 * SNS 회원 가입
	 */
	public User addSocialUser(String email, String username, CoreConst.ACCOUNT_TYPE providerId, String providerUserId,
							  String footballClub, String about, String userPictureId, String largePictureUrl) {

		UserPicture userPicture = null;

		User user = User.builder()
				.email(email.trim())
				.username(username)
				.providerId(providerId)
				.providerUserId(providerUserId)
				.roles(Collections.singletonList(CommonRole.ROLE_NUMBER_USER_01))
				.lastLogged(LocalDateTime.now())
				.build();

		if (StringUtils.isNotBlank(footballClub)) {
			FootballClub supportFC = footballClubRepository.findOneById(footballClub)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(supportFC);
		}

		if (StringUtils.isNotBlank(about))
			user.setAbout(about.trim());

		// 직접 올린 사진을 User와 연동
		if (StringUtils.isNotBlank(userPictureId)) {
			userPicture = userPictureRepository.findOneById(userPictureId)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));

			user.setUserPicture(userPicture);
		}
		// SNS에서 사용중인 사진을 User와 연동
		else if (StringUtils.isNotBlank(largePictureUrl)) {

			try {
				FileUtils.FileInfo fileInfo = FileUtils.getBytesByUrl(largePictureUrl);

				if (! StringUtils.startsWithIgnoreCase(fileInfo.getContentType(), "image/"))
					throw new ServiceException(ServiceError.FILE_ONLY_IMAGE_TYPE_CAN_BE_UPLOADED);

				userPicture = UserPicture.builder()
						.status(CoreConst.GALLERY_STATUS_TYPE.TEMP)
						.contentType(fileInfo.getContentType())
						.sourceType(providerId)
						.build();

				userPictureRepository.save(userPicture);

				ObjectId objectId = new ObjectId(userPicture.getId());
				LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				FileUtils.writeImageFile(storageProperties.getUserPictureLargePath(), localDate, userPicture.getId(), fileInfo.getContentType(),
						fileInfo.getContentLength(), fileInfo.getBytes());
				FileUtils.writeSmallImageFile(storageProperties.getUserPictureSmallPath(), localDate, userPicture.getId(), fileInfo.getContentType(),
						CoreConst.USER_SMALL_PICTURE_SIZE_WIDTH, CoreConst.USER_SMALL_PICTURE_SIZE_HEIGHT, fileInfo.getBytes());

				user.setUserPicture(userPicture);

			} catch (IOException e) {
				throw new ServiceException(ServiceError.IO_EXCEPTION, e);
			}
		}

		userRepository.save(user);

		// userImage를 user와 연동 및 활성화 처리
		if (Objects.nonNull(userPicture)) {
			userPicture.setUser(user);
			userPicture.setStatus(CoreConst.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.info("social user created. email:{} username:{}, providerId:{}", user.getEmail(), user.getUsername(), user.getProviderId());

		return user;
	}

	public User editUserProfile(String userId, String email, String username, String footballClubId, String about,
								String userPictureId) {

		UserPicture userPicture = null;

		User user = userRepository.findOneById(userId)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));

		if (StringUtils.isNotBlank(email))
			user.setEmail(StringUtils.trim(email));

		if (StringUtils.isNotBlank(username))
			user.setUsername(StringUtils.trim(username));

		if (StringUtils.isNotBlank(footballClubId)) {

			FootballClub footballClub = footballClubRepository.findOneById(footballClubId)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(footballClub);
		}

		if (StringUtils.isNotBlank(about))
			user.setAbout(StringUtils.trim(about));

		if (StringUtils.isNotBlank(userPictureId)) {
			userPicture = userPictureRepository.findOneById(userPictureId)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));

			user.setUserPicture(userPicture);
		}

		userRepository.save(user);

		if (Objects.nonNull(userPicture)) {
			userPicture.setUser(user);
			userPicture.setStatus(CoreConst.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.debug("User edited. user={}", user);

		return user;
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

	/**
	 * 프로필 이미지 올리기
	 */
	public UserPicture uploadUserPicture(String contentType, long size, byte[] bytes) {

		UserPicture userPicture = UserPicture.builder()
				.status(CoreConst.GALLERY_STATUS_TYPE.TEMP)
				.contentType(contentType)
				.sourceType(CoreConst.ACCOUNT_TYPE.JAKDUK)
				.build();

		userPictureRepository.save(userPicture);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			FileUtils.writeImageFile(storageProperties.getUserPictureLargePath(), localDate, userPicture.getId(), contentType, size, bytes);
			FileUtils.writeSmallImageFile(storageProperties.getUserPictureSmallPath(), localDate, userPicture.getId(), contentType,
					CoreConst.USER_SMALL_PICTURE_SIZE_WIDTH, CoreConst.USER_SMALL_PICTURE_SIZE_HEIGHT, bytes);

			return userPicture;

		} catch (IOException e) {
			throw new ServiceException(ServiceError.GALLERY_IO_ERROR, e);
		}
	}

	/**
	 * 내 프로필 정보 보기
	 */
	public UserProfileResponse getProfileMe(String language, String id) {

		UserProfile user = userProfileRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));

		UserProfileResponse response = UserProfileResponse.builder()
				.email(user.getEmail())
				.username(user.getUsername())
				.about(user.getAbout())
				.providerId(user.getProviderId())
				.temporaryEmail(ApiUtils.isTempararyEmail(user.getEmail()))
				.build();

		FootballClub footballClub = user.getSupportFC();
		UserPicture userPicture = user.getUserPicture();

		if (Objects.nonNull(footballClub)) {
			LocalName localName = ApiUtils.getLocalNameOfFootballClub(footballClub, language);

			response.setFootballClubName(localName);
		}

		if (Objects.nonNull(userPicture)) {
			UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture,
					authUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
					authUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

			response.setPicture(userPictureInfo);
		}

		return response;
	}

	/**
	 * 마지막 로그인 날짜 갱신
	 */
	public void updateLastLogged(String id) {

		User user = userRepository.findOneById(id)
				.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));

		user.setLastLogged(LocalDateTime.now());
		userRepository.save(user);
	}

	public void deleteUser(String id) {
		userRepository.delete(id);
	}

}
