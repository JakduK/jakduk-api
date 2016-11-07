package com.jakduk.core.service;

import com.jakduk.core.authentication.common.JakdukUserDetail;
import com.jakduk.core.authentication.common.SocialUserDetail;
import com.jakduk.core.model.db.Sequence;
import com.jakduk.core.model.db.Token;
import com.jakduk.core.model.etc.AuthUserProfile;
import com.jakduk.core.repository.SequenceRepository;
import com.jakduk.core.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 공통으로 쓰이는 서비스
 */

@Slf4j
@Service
public class CommonService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private SequenceRepository sequenceRepository;

	@Autowired
	private TokenRepository tokenRepository;

	/**
	 * 차기 SEQUENCE를 가져온다.
	 * @param name 게시판 ID
	 * @return 다음 글번호
	 */
	public Integer getNextSequence(String name) {
		
		Integer nextSeq = 1;
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.inc("seq", 1);
		
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		
		Sequence sequence = mongoTemplate.findAndModify(query, update, options, Sequence.class);
		
		if (sequence == null) {
			Sequence newSequence = new Sequence();
			newSequence.setName(name);
			sequenceRepository.save(newSequence);
			log.debug("sequence is Null. Insert new Sequence.");
			
			return nextSeq;
		} else {
			nextSeq = sequence.getSeq();
			return nextSeq;
		}
	}

	public String getLanguageCode(Locale locale, String lang) {
		
		String getLanguage = Locale.ENGLISH.getLanguage();
		
		if (StringUtils.isEmpty(lang))
			lang = locale.getLanguage();

		if (StringUtils.isNotEmpty(lang) && lang.contains(Locale.KOREAN.getLanguage()))
			getLanguage = Locale.KOREAN.getLanguage();

		return getLanguage;
	}
	
	// 손님인지 검사.
	public Boolean isAnonymousUser() {
		Boolean result = false;
		
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			result = true;
		}
		
		if (!result) {
			Collection<? extends GrantedAuthority> authoritys = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			
			for (GrantedAuthority authority : authoritys) {
				if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}

	/**
	 * 로그인 중인 회원이 관리자인지 검사.
	 * @return 관리자 이면 true
     */
	public boolean isAdmin() {
		boolean result = false;
		
		if (! SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			return false;

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		for (GrantedAuthority authority : authorities) {
			if (authority.getAuthority().equals("ROLE_ROOT")) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	/**
	 * 로그인 중인 회원이 USER 권한인지 검사.
	 * @return 회원이면 true
     */
	//
	public boolean isUser() {
		boolean result = false;

		if (! SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
			return false;

		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

		for (GrantedAuthority grantedAuthority : authorities) {
			String authority = grantedAuthority.getAuthority();
			if (authority.equals("ROLE_USER_01") || authority.equals("ROLE_USER_02") || authority.equals("ROLE_USER_03")) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * 로그인 중인 회원이 이메일 기반인지 검사.
	 * @return 이메일 기반이면 true, 아니면 false
     */
	public boolean isJakdukUser() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukUserDetail;
	}

	/**
	 * 로그인 중인 회원이 소셜 기반인지 검사.
	 * @return 이메일 기반이면 true, 아니면 false
	 */
	public boolean isSocialUser() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail;
	}

	// 숫자인지 체크
	public boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile("[+-]?\\d+");
		return pattern.matcher(str).matches();
	}

	/**
	 * ResourceBundle에서 메시지 가져오기.
	 * @param locale
	 * @param bundle
	 * @param getString
     * @return
     */

	@Deprecated
	public String getResourceBundleMessage(Locale locale, String bundle, String getString, Object... params) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, locale);
		return MessageFormat.format(resourceBundle.getString(getString), params);
	}

	/**
	 * ResourceBundle에서 메시지 가져오기.
	 * @param bundle 번들 이름 ex) messages.common
	 * @param getString 메시지 이름 ex) common.exception.you.are.writer
	 * @param params 파라미터가 있을 경우 계속 넣을 수 있음
     * @return 언어별 메시지 결과 반환
     */
	public String getResourceBundleMessage(String bundle, String getString, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, locale);
		return MessageFormat.format(resourceBundle.getString(getString), params);
	}

	// 토큰 가져오기.
	public Token getTokenByCode(String code) {
		return tokenRepository.findByCode(code);
	}

	/**
	 * 로그인 중인 회원 정보를 가져온다.
	 * @return 회원 객체
	 */
	public AuthUserProfile getAuthUserProfile() {
		AuthUserProfile authUserProfile = null;

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SocialUserDetail) {
				SocialUserDetail userDetail = (SocialUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				List<String> roles = new ArrayList<>();

				for (GrantedAuthority grantedAuthority : authorities) {
					String authority = grantedAuthority.getAuthority();
					roles.add(authority);
				}

				authUserProfile = AuthUserProfile.builder()
						.id(userDetail.getId())
						.email(userDetail.getUserId())
						.username(userDetail.getUsername())
						.providerId(userDetail.getProviderId())
						.roles(roles)
						.build();
			} else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukUserDetail) {
				JakdukUserDetail principal = (JakdukUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
				List<String> roles = new ArrayList<>();

				for (GrantedAuthority grantedAuthority : authorities) {
					String authority = grantedAuthority.getAuthority();
					roles.add(authority);
				}

				authUserProfile = AuthUserProfile.builder()
						.id(principal.getId())
						.email(principal.getUsername())
						.username(principal.getNickname())
						.providerId(principal.getProviderId())
						.roles(roles)
						.build();
			}
		}

		return authUserProfile;
	}
}
