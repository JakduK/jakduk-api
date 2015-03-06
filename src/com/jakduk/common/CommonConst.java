package com.jakduk.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public class CommonConst {
	
	public final static String COMMON_TYPE_SET = "set";
	public final static String COMMON_TYPE_CANCEL = "cancel";
	
	/**
	 * 게시판 종류.
	 */
	public final static String BOARD_NAME_FREE = "freeBoard";
	
	/**
	 * 백과사전 언어 구분.
	 */
	public final static String ENCYCLOPEDIA_EN = "encyclopediaEn";
	public final static String ENCYCLOPEDIA_KO = "encyclopediaKo";
	
	/**
	 * 화면에 보여질 목록 갯수
	 */
	public final static Integer BOARD_SIZE_LINE_NUMBER = 15;
	public final static Integer HOME_SIZE_LINE_NUMBER = 5;
	public final static Integer HOME_SIZE_POST = 7;
	public final static Integer HOME_SIZE_GALLERY = 6;
	public final static Integer RSS_SIZE_ITEM = 200;
	public final static Integer HOME_COMMENT_CONTENT_MAX_LENGTH = 120;
	public final static Integer COMMENT_MAX_SIZE = 30;	
	public final static Integer GALLERY_SIZE = 12;

	/**
	 * 게시물 쿠키의 만료되는 시간(초)
	 */
	public final static Integer BOARD_COOKIE_EXPIRE_SECONDS = 30;
	public final static String COOKIE_NAME_BOARD_FREE = "freeBoard";
	public final static String COOKIE_NAME_GALLERY = "gallery";
	
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
	
	/**
	 * 글의 좋아요 싫어요에 대한 에러 코드
	 */
	public final static String BOARD_USERS_FEELINGS_STATUS_NONE = "none";
	public final static String BOARD_USERS_FEELINGS_STATUS_ALREADY = "already";
	public final static String BOARD_USERS_FEELINGS_STATUS_LIKE = "like";
	public final static String BOARD_USERS_FEELINGS_STATUS_DISLIKE = "dislike";
	public final static String BOARD_USERS_FEELINGS_STATUS_WRITER = "writer";
	public final static String BOARD_USERS_FEELINGS_STATUS_ANONYMOUS = "anonymous";
	
	/**
	 * 글의 좋아요, 싫어요
	 */
	public final static String FEELING_TYPE_LIKE = "like";
	public final static String FEELING_TYPE_DISLIKE = "dislike";
	
	public final static String PRINCIPAL_ANONYMOUSUSER = "anonymousUser";
	
	/**
	 * 글 지움 종류
	 */
	public final static String BOARD_DELETE_TYPE_POSTONLY = "postonly";
	public final static String BOARD_DELETE_TYPE_ALL = "all";
	
	/**
	 * 글 히스토리 상태
	 */
	public final static String BOARD_HISTORY_TYPE_CREATE = "create";
	public final static String BOARD_HISTORY_TYPE_EDIT = "edit";
	public final static String BOARD_HISTORY_TYPE_DELETE = "delete";
	public final static String BOARD_HISTORY_TYPE_NOTICE = "notice";
	public final static String BOARD_HISTORY_TYPE_CANCEL_NOTICE = "c.notice";
	
	/**
	 * 사진 썸네일 크기.
	 */
	public final static Integer GALLERY_THUMBNAIL_SIZE_WIDTH = 360;
	public final static Integer GALLERY_THUMBNAIL_SIZE_HEIGHT = 230;
	
	/**
	 * 사진 상태값.
	 */
	public final static String GALLERY_STATUS_TEMP = "temp";
	public final static String GALLERY_STATUS_USE = "use";
	public final static String GALLERY_STATUS_UNUSE = "unuse";
	
	/**
	 * 사진 이름 입력 상태값.
	 */
	public final static String GALLERY_NAME_STATUS_INPUT = "input";
	public final static String GALLERY_NAME_STATUS_SUBJECT = "subject";
	
	/**
	 * 클라이언트 종류.
	 */
	public final static String DEVICE_TYPE_NORMAL = "normal";
	public final static String DEVICE_TYPE_MOBILE = "mobile";
	public final static String DEVICE_TYPE_TABLET = "tablet";
	
	public final static long GALLERY_MAXIUM_CAPACITY = 1048576; // Byte
}
