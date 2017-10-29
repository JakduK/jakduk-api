package com.jakduk.api.service;


import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.AuthUtils;
import com.jakduk.api.common.util.FileUtils;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.Token;
import com.jakduk.api.model.db.User;
import com.jakduk.api.model.db.UserPicture;
import com.jakduk.api.model.embedded.LocalName;
import com.jakduk.api.model.embedded.UserPictureInfo;
import com.jakduk.api.model.simple.UserOnPasswordUpdate;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.model.simple.UserSimple;
import com.jakduk.api.repository.TokenRepository;
import com.jakduk.api.repository.footballclub.FootballClubRepository;
import com.jakduk.api.repository.user.UserPictureRepository;
import com.jakduk.api.repository.user.UserProfileRepository;
import com.jakduk.api.repository.user.UserRepository;
import com.jakduk.api.restcontroller.vo.user.UserPasswordFindResponse;
import com.jakduk.api.restcontroller.vo.user.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

	@Resource private JakdukProperties.Storage storageProperties;
	@Resource private AuthUtils authUtils;

	@Autowired private RabbitMQPublisher rabbitMQPublisher;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private UserRepository userRepository;
	@Autowired private FootballClubRepository footballClubRepository;
	@Autowired private UserProfileRepository userProfileRepository;
	@Autowired private UserPictureRepository userPictureRepository;
	@Autowired private TokenRepository tokenRepository;

	public Optional<User> findOneByProviderIdAndProviderUserId(Constants.ACCOUNT_TYPE providerId, String providerUserId) {
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

	public List<UserSimple> findSimpleUsers() {
		return userRepository.findSimpleUsers();
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
		if (StringUtils.isAllBlank(user.getEmail(), snsEmail)) {
			user.setEmail(JakdukUtils.generateTemporaryEmail());
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

	public User createJakdukUser(String email, String username, String password, String footballClub, String about, String userPictureId) {

		UserPicture userPicture = null;

		User user = User.builder()
				.email(email)
				.username(username)
				.password(passwordEncoder.encode(password))
				.providerId(Constants.ACCOUNT_TYPE.JAKDUK)
				.roles(Collections.singletonList(JakdukAuthority.ROLE_USER_01.getCode()))
				.lastLogged(LocalDateTime.now())
				.build();

		this.setUserAdditionalInfo(user, footballClub, about);

		if (StringUtils.isNotBlank(userPictureId)) {
			userPicture = userPictureRepository.findOneById(userPictureId.trim())
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER_IMAGE));

			user.setUserPicture(userPicture);
		}

		userRepository.save(user);

		if (Objects.nonNull(userPicture)) {
			userPicture.setStatus(Constants.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.info("JakduK user created. {}", user);

		return user;
	}

	/**
	 * SNS 회원 가입
	 */
	public User createSocialUser(String email, String username, Constants.ACCOUNT_TYPE providerId, String providerUserId,
								 String footballClub, String about, String userPictureId, String largePictureUrl) {

		UserPicture userPicture = null;

		User user = User.builder()
				.email(email)
				.username(username)
				.providerId(providerId)
				.providerUserId(providerUserId)
				.roles(Collections.singletonList(JakdukAuthority.ROLE_USER_02.getCode()))
				.lastLogged(LocalDateTime.now())
				.build();

		this.setUserAdditionalInfo(user, footballClub, about);

		// 직접 올린 사진을 User와 연동
		if (StringUtils.isNotBlank(userPictureId)) {
			userPicture = userPictureRepository.findOneById(userPictureId.trim())
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
						.status(Constants.GALLERY_STATUS_TYPE.TEMP)
						.contentType(fileInfo.getContentType())
						.build();

				userPictureRepository.save(userPicture);

				ObjectId objectId = new ObjectId(userPicture.getId());
				LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				FileUtils.writeImageFile(storageProperties.getUserPictureLargePath(), localDate, userPicture.getId(), fileInfo.getContentType(),
						fileInfo.getContentLength(), fileInfo.getBytes());
				FileUtils.writeSmallImageFile(storageProperties.getUserPictureSmallPath(), localDate, userPicture.getId(), fileInfo.getContentType(),
						Constants.USER_SMALL_PICTURE_SIZE_WIDTH, Constants.USER_SMALL_PICTURE_SIZE_HEIGHT, fileInfo.getBytes());

				user.setUserPicture(userPicture);

			} catch (IOException e) {
				throw new ServiceException(ServiceError.IO_EXCEPTION, e);
			}
		}

		userRepository.save(user);

		if (Objects.nonNull(userPicture)) {
			userPicture.setStatus(Constants.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.info("social user created. {}", user);

		return user;
	}

	private void setUserAdditionalInfo(User user, String footballClub, String about) {
		if (StringUtils.isNotBlank(footballClub)) {
			FootballClub supportFC = footballClubRepository.findOneById(footballClub.trim())
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_FOOTBALL_CLUB));

			user.setSupportFC(supportFC);
		}

		if (StringUtils.isNotBlank(about))
			user.setAbout(about.trim());
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
			userPicture.setStatus(Constants.GALLERY_STATUS_TYPE.ENABLE);
			userPictureRepository.save(userPicture);
		}

		log.debug("User edited. user={}", user);

		return user;
	}

	/**
	 * 이메일 기반 회원의 비밀번호 변경.
	 *
	 * @param newPassword 새 비밀번호
     */
	public void updateUserPassword(String userId, String newPassword) {
		User user = userRepository.findOneById(userId).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));
		user.setPassword(passwordEncoder.encode(newPassword.trim()));

		userRepository.save(user);

		log.info("jakduk user password changed. email={}, username={}", user.getEmail(), user.getUsername());
	}

	public void userPasswordUpdateByEmail(String email, String password) {
		User user = userRepository.findOneByEmail(email).orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));
		user.setPassword(passwordEncoder.encode(password.trim()));

		userRepository.save(user);

		log.info("jakduk user password changed. email={}, username={}", user.getEmail(), user.getUsername());
	}

	public UserPasswordFindResponse sendEmailToResetPassword(String email, String host) {
		String message = "";

		Optional<UserProfile> optUserProfile = userProfileRepository.findOneByEmail(email);

		if (optUserProfile.isPresent()) {
			switch (optUserProfile.get().getProviderId()) {
				case JAKDUK:
					message = JakdukUtils.getMessageSource("user.msg.reset.password.send.email");
					rabbitMQPublisher.sendResetPassword(JakdukUtils.getLocale(), email, host);
					break;
				case DAUM:
					message = JakdukUtils.getMessageSource("user.msg.you.connect.with.sns", Constants.ACCOUNT_TYPE.DAUM);
					break;
				case FACEBOOK:
					message = JakdukUtils.getMessageSource("user.msg.you.connect.with.sns", Constants.ACCOUNT_TYPE.FACEBOOK);
					break;
			}
		} else {
			message = JakdukUtils.getMessageSource( "user.msg.you.are.not.registered");
		}

		return UserPasswordFindResponse.builder()
				.subject(email)
				.message(message)
				.build();
	}

	public UserPasswordFindResponse resetPasswordWithToken(String code, String password) {
		Optional<Token> oToken = tokenRepository.findOneByCode(code);

		String message;
		String subject = null;

		if (oToken.isPresent()) {
			Token token = oToken.get();

			User user = userRepository.findOneByEmail(token.getEmail())
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND_USER));

			user.setPassword(passwordEncoder.encode(password.trim()));

			userRepository.save(user);

			log.info("jakduk user password changed. email={}, username={}", user.getEmail(), user.getUsername());

			subject = token.getEmail();
			message = JakdukUtils.getMessageSource("user.msg.success.change.password");

			tokenRepository.delete(token);
		} else {
			message = JakdukUtils.getMessageSource("user.msg.reset.password.invalid");
		}

		return UserPasswordFindResponse.builder()
				.subject(subject)
				.message(message)
				.build();
	}

	/**
	 * 프로필 이미지 올리기
	 */
	public UserPicture uploadUserPicture(String contentType, long size, byte[] bytes) {

		UserPicture userPicture = UserPicture.builder()
				.status(Constants.GALLERY_STATUS_TYPE.TEMP)
				.contentType(contentType)
				.build();

		userPictureRepository.save(userPicture);

		ObjectId objectId = new ObjectId(userPicture.getId());
		LocalDate localDate = objectId.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		try {
			FileUtils.writeImageFile(storageProperties.getUserPictureLargePath(), localDate, userPicture.getId(), contentType, size, bytes);
			FileUtils.writeSmallImageFile(storageProperties.getUserPictureSmallPath(), localDate, userPicture.getId(), contentType,
					Constants.USER_SMALL_PICTURE_SIZE_WIDTH, Constants.USER_SMALL_PICTURE_SIZE_HEIGHT, bytes);

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
				.temporaryEmail(JakdukUtils.isTempararyEmail(user.getEmail()))
				.build();

		FootballClub footballClub = user.getSupportFC();
		UserPicture userPicture = user.getUserPicture();

		if (Objects.nonNull(footballClub)) {
			LocalName localName = JakdukUtils.getLocalNameOfFootballClub(footballClub, language);

			response.setFootballClubName(localName);
		}

		if (Objects.nonNull(userPicture)) {
			UserPictureInfo userPictureInfo = new UserPictureInfo(userPicture.getId(),
					authUtils.generateUserPictureUrl(Constants.IMAGE_SIZE_TYPE.SMALL, userPicture.getId()),
					authUtils.generateUserPictureUrl(Constants.IMAGE_SIZE_TYPE.LARGE, userPicture.getId()));

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
