package com.jakduk.service;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.common.JakdukPrincipal;
import com.jakduk.authentication.common.SocialUserDetail;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Sequence;
import com.jakduk.model.db.Token;
import com.jakduk.model.etc.AuthUserProfile;
import com.jakduk.repository.SequenceRepository;
import com.jakduk.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
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

	@Value("${deny.redirect.url}")
	private String denyRedirectUrl;

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
	
	/**
	 * 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
	 * @param request
	 * @param response
	 * @param prefix
	 * @param id
     * @return 쿠키를 새로 저장했다면 true, 아니면 false.
     */
	public boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, String prefix, String id) {
		
		Boolean findSameCookie = false;
		String cookieName = prefix + "_" + id;
		
		Cookie cookies[] = request.getCookies();
		
		if (cookies != null) {
			for (int i = 0 ; i < cookies.length ; i++) {
				String name = cookies[i].getName();
				
				if (cookieName.equals(name)) {
					findSameCookie = true;
					break;
				}
			}
		}
		
		if (findSameCookie == false) {
			Cookie cookie = new Cookie(cookieName, "r");
			cookie.setMaxAge(CommonConst.BOARD_COOKIE_EXPIRE_SECONDS);
			response.addCookie(cookie);
			
			return true;
		} else {
			return false;
		}
	}
	
	public String getLanguageCode(Locale locale, String lang) {
		
		String getLanguage = Locale.ENGLISH.getLanguage();
		
		if (lang == null || lang.isEmpty()) {
			lang = locale.getLanguage();
		}
		
		if (lang != null) {
			if (lang.contains(Locale.KOREAN.getLanguage())) {
				getLanguage = Locale.KOREAN.getLanguage();
			}		
		}
		
		return getLanguage;
	}
	
	public Map<String, String> getDateTimeFormat(Locale locale) {
		
		HashMap<String, String> dateTimeFormat = new HashMap<String, String>();
		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, locale);
		SimpleDateFormat sf = (SimpleDateFormat) df;
		
		dateTimeFormat.put("dateTime", sf.toPattern());
		
		df = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
		sf = (SimpleDateFormat) df;
		
		dateTimeFormat.put("date", sf.toPattern());
		
		df = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
		sf = (SimpleDateFormat) df;
		
		dateTimeFormat.put("time", sf.toPattern());
		
		return dateTimeFormat;
	}

	public void setCookie(HttpServletResponse response, String name, String value, String path) {
		try {
			Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
			cookie.setMaxAge(CommonConst.COOKIE_EMAIL_MAX_AGE); // a day
			cookie.setPath(path);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}
	
	public void releaseCookie(HttpServletResponse response, String name, String path) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0); // remove
		cookie.setPath(path);
		response.addCookie(cookie);		
	}

	// 손님인지 검사.
	public Boolean isAnonymousUser() {
		Boolean result = false;
		
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() == false) {
			result = true;
		}
		
		if (result == false) {
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

	// 관리자인지 검사.
	public Boolean isAdmin() {
		Boolean result = false;
		
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() == false) {
			result = true;
		}
		
		if (result == false) {
			Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			
			for (GrantedAuthority authority : authorities) {
				if (authority.getAuthority().equals("ROLE_ROOT")) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}

	// 회원인지 검사.
	public Boolean isUser() {
		Boolean result = false;

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() == false) {
			result = true;
		}

		if (result == false) {
			Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

			for (GrantedAuthority grantedAuthority : authorities) {
				String authority = grantedAuthority.getAuthority();
				if (authority.equals("ROLE_USER_01") || authority.equals("ROLE_USER_02") || authority.equals("ROLE_USER_03")) {
					result = true;
					break;
				}
			}
		}

		return result;
	}

	public Boolean isRedirectUrl(String url) {
		Boolean result = true;
		
		String[] deny = denyRedirectUrl.split(",");
		
		for(int idx = 0 ; idx < deny.length ; idx++) {
			if (url.contains(deny[idx])) {
				result = false;
				break;
			}	
		}
		
		return result;
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
	 * @param bundle
	 * @param getString
	 * @param params
     * @return
     */
	public String getResourceBundleMessage(String bundle, String getString, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle, locale);
		return MessageFormat.format(resourceBundle.getString(getString), params);
	}

	/**
	 * 모바일 디바이스 정보 가져오기.
	 * @param device
	 * @return
     */
	public CommonConst.DEVICE_TYPE getDeviceInfo(Device device) {
		if (device.isNormal()) {
			return CommonConst.DEVICE_TYPE.NORMAL;
		} else if (device.isMobile()) {
			return CommonConst.DEVICE_TYPE.MOBILE;
		} else if (device.isTablet()) {
			return CommonConst.DEVICE_TYPE.TABLET;
		} else {
			return CommonConst.DEVICE_TYPE.NORMAL;
		}
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
			} else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof JakdukPrincipal) {
				JakdukPrincipal principal = (JakdukPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
