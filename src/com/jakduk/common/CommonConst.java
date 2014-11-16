package com.jakduk.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public class CommonConst {
	
	/**
	 * 게시판 종류. SEQUNCE 생성에도 쓰임.
	 */
	public final static String BOARD_NAME_FREE = "freeBoard";
	
	public final static String ENCYCLOPEDIA_EN = "encyclopediaEn";
	public final static String ENCYCLOPEDIA_KO = "encyclopediaKo";
	
	/**
	 * 게시물 목록에서 보여지는 게시물 갯수
	 */
	public final static Integer BOARD_LINE_NUMBER = 10;

	/**
	 * 게시물 쿠키의 만료되는 시간(초)
	 */
	public final static Integer BOARD_COOKIE_EXPIRE_SECONDS = 30;
	
	/**
	 * 게시판 말머리.
	 */
	public final static String BOARD_CATEGORY_NONE = "none";
	public final static String BOARD_CATEGORY_FREE = "free";
	public final static String BOARD_CATEGORY_FOOTBALL = "football";
	public final static String BOARD_CATEGORY_DEVELOP = "develop";
	public final static String BOARD_CATEGORY_ALL = "all";
	
	/**
	 * 사용자 쿠키
	 */
	public final static String COOKIE_EMAIL = "email";
	public final static String COOKIE_REMEMBER = "remember";
	
	/**
	 * OAuth 종류
	 */
	public final static String OAUTH_TYPE_FACEBOOK = "facebook";
	public final static String OAUTH_TYPE_DAUM = "daum";
	public final static String AUTH_TYPE_JAKDUK = "jakduk";
	
	/**
	 * OAuth 추가 정보 입력 여부
	 */
	public final static String OAUTH_ADDITIONAL_INFO_STATUS_NONE = "none";
	public final static String OAUTH_ADDITIONAL_INFO_STATUS_UNUSE = "unuse";
	public final static String OAUTH_ADDITIONAL_INFO_STATUS_BLANK = "blank";
	public final static String OAUTH_ADDITIONAL_INFO_STATUS_OK = "ok";
	
	/**
	 * 축구단 상태
	 */
	public final static String FOOTBALL_CLUB_STATUS_NONE = "none";
	public final static String FOOTBALL_CLUB_STATUS_ACTIVE = "active";
	public final static String FOOTBALL_CLUB_STATUS_INACTIVE = "inactive";
	
	public final static String BOARD_USERS_FEELINGS_STATUS_NONE = "none";
	public final static String BOARD_USERS_FEELINGS_STATUS_ALREADY = "already";
	public final static String BOARD_USERS_FEELINGS_STATUS_LIKE = "like";
	public final static String BOARD_USERS_FEELINGS_STATUS_DISLIKE = "dislike";
	public final static String BOARD_USERS_FEELINGS_STATUS_WRITER = "writer";
	public final static String BOARD_USERS_FEELINGS_STATUS_ANONYMOUS = "anonymous";
	
	public final static String BOARD_USERS_FEELINGS_TYPE_LIKE = "like";
	public final static String BOARD_USERS_FEELINGS_TYPE_DISLIKE = "dislike";
	
	public final static String PRINCIPAL_ANONYMOUSUSER = "anonymousUser";
	
}
