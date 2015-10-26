package com.jakduk.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jakduk.authentication.common.CommonUserDetails;
import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Sequence;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.SequenceRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 공통으로 쓰이는 서비스
 */

@Service
public class CommonService {
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private FootballClubRepository footballClubRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
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
			logger.debug("sequence is Null. Insert new Sequence.");
			
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
	 * @param boardName 게시판 이름. CommonConst.BOARD_NAME_XXXX
	 * @param seq 게시물 번호
	 * @return 쿠키를 새로 저장했다면 true, 아니면 false. 
	 */
	public Boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, String prefix, String id) {
		
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
	
	public void doOAuthAutoLogin(OAuthPrincipal principal, Object credentials, CommonUserDetails userDetails) {
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, credentials, principal.getAuthorities());
		
		if (userDetails != null) {
			token.setDetails(userDetails);
		}
		
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	public void doJakdukAutoLogin(JakdukPrincipal principal, Object credentials) {
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(principal, credentials, principal.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
	public void setCookie(HttpServletResponse response, String name, String value, String path) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(CommonConst.COOKIE_EMAIL_MAX_AGE); // a day
		cookie.setPath(path);
		response.addCookie(cookie);		
	}
	
	public void releaseCookie(HttpServletResponse response, String name, String path) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0); // remove
		cookie.setPath(path);
		response.addCookie(cookie);		
	}
	
	public Boolean isAnonymousUser() {
		Boolean result = false;
		
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
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
	
	public Boolean isAdmin() {
		Boolean result = false;
		
		if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			result = true;
		}
		
		if (result == false) {
			Collection<? extends GrantedAuthority> authoritys = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			
			for (GrantedAuthority authority : authoritys) {
				if (authority.getAuthority().equals("ROLE_ROOT")) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}	
	
	public Boolean isRedirectUrl(String url) {
		Boolean result = true;
		
		String[] deny = {"login", "logout", "j_spring", "write", "admin"};
		
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
    
}