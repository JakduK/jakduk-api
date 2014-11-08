package com.jakduk.service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jakduk.authentication.common.CommonUserDetails;
import com.jakduk.authentication.common.OAuthPrincipal;
import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.Sequence;
import com.jakduk.model.web.BoardPageInfo;
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
	 * 게시판 목록의 페이지에 대한 정보를 넘겨준다.
	 * @param page 현재 페이지 번호
	 * @param numberPosts 전체 게시물 갯수
	 * @param numberPages 전체 페이지 수
	 * @return BoardPageInfo 객체
	 */
	public BoardPageInfo getCountPages(Long page, Long numberPosts, Integer numberPages) {
		
		BoardPageInfo boardPageInfo = new BoardPageInfo();

		Long tmpVal = (long) 0;
		Long totalPages = (long) 1;		
		Long prevPage = (long) 1;
		Long nextPage = (long) 1;
		
		if((numberPosts - CommonConst.BOARD_LINE_NUMBER) > 0) {
			tmpVal = (long) (numberPosts / CommonConst.BOARD_LINE_NUMBER);
			
			if((numberPosts % CommonConst.BOARD_LINE_NUMBER) == 0) {
				totalPages = tmpVal;
			} else {
				totalPages = tmpVal + 1;
			}
		}
		
		Long startPage = page - (page % numberPages) + 1;
		
		if (startPage <= 0) {
			startPage ++;
		}
		
		if (startPage > numberPages) {
			prevPage = startPage - numberPages;
		} else {
			prevPage = (long) -1;
		}
		
		Long endPage = (long) 1;
		
		if ((totalPages - page) > numberPages) {
			endPage = startPage + numberPages - 1;
			nextPage = startPage + numberPages;
		} else {
			endPage = totalPages;
			nextPage = (long) -1;
		}
		
		boardPageInfo.setStartPage(startPage);
		boardPageInfo.setEndPage(endPage);
		boardPageInfo.setPrevPage(prevPage);
		boardPageInfo.setNextPage(nextPage);
		
		return boardPageInfo;
	}
	
	/**
	 * 게시물의 쿠키를 저장한다. 이미 있다면 저장하지 않는다.
	 * @param request
	 * @param response
	 * @param boardName 게시판 이름. CommonConst.BOARD_NAME_XXXX
	 * @param seq 게시물 번호
	 * @return 쿠키를 새로 저장했다면 true, 아니면 false. 
	 */
	public Boolean addViewsCookie(HttpServletRequest request, HttpServletResponse response, String boardName, int seq) {
		
		Boolean findSameCookie = false;
		String cookieName = boardName + "_" + seq;
		
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
	
	public List<FootballClub> getFootballClubs(String language) {
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("names"));
		Pageable pageable = new PageRequest(0, 100, sort);
		
		List<FootballClub> footballClubs = footballClubRepository.findByNamesLanguage(language, pageable);
		
		return footballClubs;
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
		
		logger.debug("phjang=" + token);
		
		SecurityContextHolder.getContext().setAuthentication(token);
	}
	
}