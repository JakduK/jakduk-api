package com.jakduk.api.service;


import com.jakduk.api.common.util.ApiUtils;
import com.jakduk.api.common.util.JwtTokenUtils;
import com.jakduk.api.common.util.UserUtils;
import com.jakduk.api.vo.user.UserProfileResponse;
import com.jakduk.core.common.CommonRole;
import com.jakduk.core.common.CoreConst;
import com.jakduk.core.common.util.FileUtils;
import com.jakduk.core.exception.ServiceError;
import com.jakduk.core.exception.ServiceException;
import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.db.User;
import com.jakduk.core.model.db.UserPicture;
import com.jakduk.core.model.embedded.LocalName;
import com.jakduk.core.model.embedded.UserPictureInfo;
import com.jakduk.core.model.simple.UserOnPasswordUpdate;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.repository.footballclub.FootballClubRepository;
import com.jakduk.core.repository.user.UserPictureRepository;
import com.jakduk.core.repository.user.UserProfileRepository;
import com.jakduk.core.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FootballClubRepository footballClubRepository;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserPictureRepository userPictureRepository;

	@Autowired
	private JwtTokenUtils jwtTokenUtils;

	@Resource
	private UserUtils userUtils;

	@Value("${core.storage.user.picture.large.path}")
	private String storageUserPictureLargePath;

	@Value("${core.storage.user.picture.small.path}")
	private String storageUserPictureSmallPath;

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
	 * SNS 가입 회원 로그인
	 *
	 * @return 인증 토큰
	 */
	public String loginSnsUser(Device device, String snsEmail, User user) {

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

//		SocialUserDetails userDetails = (SocialUserDetails) socialDetailService.loadUserByUsername(user.getEmail());
//
//		// 토큰 생성
//		String token = jwtTokenUtils.generateToken(device, userDetails.getId(), userDetails.getEmail(), userDetails.getUsername(),
//				userDetails.getProviderId().name());
//
//		return token;
		return null;
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

		log.debug("JakduK user created. user=" + user);

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

				FileUtils.writeImageFile(storageUserPictureLargePath, localDate, userPicture.getId(), fileInfo.getContentType(),
						fileInfo.getContentLength(), fileInfo.getBytes());
				FileUtils.writeSmallImageFile(storageUserPictureSmallPath, localDate, userPicture.getId(), fileInfo.getContentType(),
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

		log.debug("social user created.\n{}" + user);

		return user;
	}

	public void editUserProfile(String userId, String email, String username, String footballClubId, String about,
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

		log.debug("User edited. user=" + user);
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
			FileUtils.writeImageFile(storageUserPictureLargePath, localDate, userPicture.getId(), contentType, size, bytes);
			FileUtils.writeSmallImageFile(storageUserPictureSmallPath, localDate, userPicture.getId(), contentType,
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
					userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
					userUtils.generateUserPictureUrl(CoreConst.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

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

}
