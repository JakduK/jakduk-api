package com.jakduk.api.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.jakduk.api.common.Constants;
import com.jakduk.api.configuration.JakdukProperties;
import com.jakduk.api.configuration.security.JakdukAuthority;
import com.jakduk.api.configuration.security.UserDetailsImpl;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.restcontroller.vo.user.SessionUser;
import com.jakduk.api.restcontroller.vo.user.SocialProfile;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pyohwan
 *         16. 7. 31 오후 9:04
 */

@Component
public class AuthUtils {

	private final String FACEBOOK_PROFILE_API_URL = "https://graph.facebook.com/v2.8/me?fields=name,email,picture.type(large)&format=json";
	private final String KAKAO_PROFILE_API_URL = "https://kapi.kakao.com/v2/user/me";
	private final String NAVER_PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";
	private final String GOOGLE_PROFILE_API_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
	@Resource
	private JakdukProperties jakdukProperties;
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 손님인지 검사.
	 */
	public static Boolean isAnonymousSessionUser() {
		Boolean result = false;

		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			result = true;

		if (!result) {
			Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
				.getAuthentication()
				.getAuthorities();
			result = authorities.stream()
				.anyMatch(authority -> authority.getAuthority().equals(JakdukAuthority.ROLE_ANONYMOUS.name()));
		}

		return result;
	}

	/**
	 * 로그인 중인 회원이 관리자인지 검사.
	 * @return 관리자 이면 true
	 */
	public static Boolean isAdminSessionUser() {
		Boolean result = false;

		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			return false;

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
			.getAuthentication()
			.getAuthorities();
		result = authorities.stream().anyMatch(authority -> JakdukAuthority.isAdminAuthority(authority.getAuthority()));

		return result;
	}

	/**
	 * 로그인 중인 회원이 USER 권한인지 검사.
	 *
	 * @return 회원이면 true
	 */
	public static Boolean isSessionUserRole() {
		Boolean result = false;

		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			return false;

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext()
			.getAuthentication()
			.getAuthorities();
		result = authorities.stream().anyMatch(authority -> JakdukAuthority.isUserAuthority(authority.getAuthority()));

		return result;
	}

	/**
	 * 로그인 중인 회원이 이메일 가입 회원 인지 검사.
	 * @return 이메일 기반이면 true, 아니면 false
	 */
	public static Boolean isJakdukSessionUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof UserDetailsImpl) {
			UserDetailsImpl userDetail = (UserDetailsImpl)authentication.getPrincipal();

			return userDetail.getProviderId().equals(Constants.ACCOUNT_TYPE.JAKDUK);
		} else {
			return false;
		}
	}

	public static Boolean isSnsSessionUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof UserDetailsImpl) {
			UserDetailsImpl userDetail = (UserDetailsImpl)authentication.getPrincipal();

			Constants.ACCOUNT_TYPE providerId = userDetail.getProviderId();

			return AuthUtils.isSnsUser(providerId);
		} else {
			return false;
		}
	}

	public static Boolean isJakdukUser(Constants.ACCOUNT_TYPE accountType) {
		switch (accountType) {
			case JAKDUK:
				return true;
			default:
				return false;
		}
	}

	public static Boolean isSnsUser(Constants.ACCOUNT_TYPE accountType) {
		switch (accountType) {
			case KAKAO:
			case FACEBOOK:
			case NAVER:
				return true;
			default:
				return false;
		}
	}

	/**
	 * 로그인 중인 회원 정보를 가져온다.
	 */
	public static SessionUser getSessionProfile() {
		SessionUser sessionUser = null;

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getPrincipal() instanceof UserDetailsImpl) {
			UserDetailsImpl userDetail = (UserDetailsImpl)authentication.getPrincipal();

			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

			List<String> roles = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

			sessionUser = new SessionUser() {{
				setId(userDetail.getId());
				setEmail(userDetail.getUsername());
				setUsername(userDetail.getNickname());
				setProviderId(userDetail.getProviderId());
				setPicture(userDetail.getPicture());
				setRoles(roles);
			}};
		}

		return sessionUser;
	}

	/**
	 * CommonWriter를 가져온다.
	 */
	public static CommonWriter getCommonWriterFromSession() {
		SessionUser sessionUser = getSessionProfile();

		if (Objects.nonNull(sessionUser)) {
			CommonWriter commonWriter = new CommonWriter();
			commonWriter.setUserId(sessionUser.getId());
			commonWriter.setUsername(sessionUser.getUsername());
			commonWriter.setProviderId(sessionUser.getProviderId());
			commonWriter.setPicture(sessionUser.getPicture());

			return commonWriter;
		} else {
			return null;
		}
	}

	public static Collection<? extends GrantedAuthority> getAuthorities(List<Integer> roles) {
		return getGrantedAuthorities(getRoles(roles));
	}

	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 세션에 security 객체를 업데이트
	 */
	public static void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
	}

	private static List<String> getRoles(List<Integer> roles) {
		return roles.stream()
			.map(JakdukAuthority::findByCode)
			.map(JakdukAuthority::name)
			.collect(Collectors.toList());
	}

	private static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}

		return authorities;
	}

	public SocialProfile getSnsProfile(Constants.ACCOUNT_TYPE accountType, String accessToken) {
		switch (accountType) {
			case FACEBOOK:
				return this.getFacebookProfile(accessToken);
			case KAKAO:
				return this.getKakaoProfile(accessToken);
			case NAVER:
				return this.getNaverProfile(accessToken);
			case GOOGLE:
				return this.getGoogleProfile(accessToken);
			default:
				throw new ServiceException(ServiceError.INVALID_ACCOUNT);
		}
	}

	/**
	 * Facebook 프로필 가져오기
	 *
	 * @param accessToken accessToken
	 */
	private SocialProfile getFacebookProfile(String accessToken) {

		JsonNode jsonNode = fetchProfile(FACEBOOK_PROFILE_API_URL, accessToken);

		SocialProfile profile = new SocialProfile();
		profile.setId(jsonNode.get("id").asText());
		profile.setNickname(jsonNode.get("name").asText());

		if (Objects.nonNull(jsonNode.get("email")))
			profile.setEmail(jsonNode.get("email").asText());

		if (jsonNode.has("picture")) {
			String largePictureUrl = jsonNode.get("picture").get("data").get("url").asText();
			profile.setPictureUrl(largePictureUrl);
		}

		return profile;
	}

	private SocialProfile getKakaoProfile(String accessToken) {

		JsonNode jsonNode = fetchProfile(KAKAO_PROFILE_API_URL, accessToken);

		SocialProfile profile = new SocialProfile();
		profile.setId(jsonNode.path("id").asText());
		profile.setNickname(jsonNode.path("properties").path("nickname").asText());
		profile.setPictureUrl(jsonNode.path("properties").path("profile_image").asText());

		if (jsonNode.has("kakao_account")) {

			if (jsonNode.path("kakao_account").path("has_email").asBoolean() &&
				jsonNode.path("kakao_account").path("has_email").asBoolean()) {

				if (StringUtils.isNotBlank(jsonNode.path("kakao_account").path("email").asText())) {
					profile.setEmail(jsonNode.path("kakao_account").path("email").asText());
				}
			}
		}

		return profile;
	}

	private SocialProfile getNaverProfile(String accessToken) {

		JsonNode jsonNode = fetchProfile(NAVER_PROFILE_API_URL, accessToken);

		SocialProfile profile = new SocialProfile();

		if (!jsonNode.has("response")) {
			throw new ServiceException(ServiceError.INVALID_ACCOUNT);
		}

		profile.setId(jsonNode.get("response").get("id").asText());

		if (jsonNode.get("response").has("nickname")) {
			String nickName = jsonNode.get("response").get("nickname").asText();

			if (StringUtils.isNotBlank(nickName)) {
				profile.setNickname(nickName);
			}
		}

		if (jsonNode.get("response").has("email")) {
			String email = jsonNode.get("response").get("email").asText();

			if (StringUtils.isNotBlank(email)) {
				profile.setEmail(email);
			}
		}

		if (jsonNode.get("response").has("profile_image")) {
			String profileImage = jsonNode.get("response").get("profile_image").asText();

			if (StringUtils.isNotBlank(profileImage)) {
				profile.setPictureUrl(profileImage);
			}
		}

		return profile;
	}

	private SocialProfile getGoogleProfile(String accessToken) {
		JsonNode jsonNode = fetchProfile(GOOGLE_PROFILE_API_URL, accessToken);

		SocialProfile profile = new SocialProfile();
		profile.setId(jsonNode.get("id").asText());

		if (jsonNode.get("verified_email").asBoolean()) {
			String email = jsonNode.get("email").asText();

			if (StringUtils.isNotBlank(email)) {
				profile.setEmail(email);
			}
		}

		if (jsonNode.has("name")) {
			String name = jsonNode.get("name").asText();

			if (StringUtils.isNotBlank(name)) {
				profile.setNickname(name);
			}
		}

		if (jsonNode.has("picture")) {
			String picture = jsonNode.get("picture").asText();

			if (StringUtils.isNotBlank(picture)) {
				profile.setPictureUrl(picture);
			}
		}

		return profile;
	}

	/**
	 * 회원 프로필 이미지 URL을 생성한다.
	 *
	 * @param sizeType size 타입
	 * @param id UserImage의 ID
	 */
	public String generateUserPictureUrl(Constants.IMAGE_SIZE_TYPE sizeType, String id) {

		if (StringUtils.isBlank(id))
			return null;

		String urlPathUserPicture = null;

		switch (sizeType) {
			case LARGE:
				urlPathUserPicture = jakdukProperties.getApiUrlPath().getUserPictureLarge();
				break;
			case SMALL:
				urlPathUserPicture = jakdukProperties.getApiUrlPath().getUserPictureSmall();
				break;
		}

		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(jakdukProperties.getApiServerUrl())
			.path("/{urlPathGallery}/{id}")
			.buildAndExpand(urlPathUserPicture, id);

		return uriComponents.toUriString();
	}

	/**
	 * accessToken에 해당하는 프로필 정보를 가져온다.
	 *
	 * @param url 요청할 URL
	 * @param accessToken accessToken
	 */
	private JsonNode fetchProfile(String url, String accessToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);

		return responseEntity.getBody();
	}

}

