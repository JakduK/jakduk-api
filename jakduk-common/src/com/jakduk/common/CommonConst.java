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

	public final static String SEQ_JAKDU_SCHEDULE_GROUP = "JAKDU_SCHEDULE_GROUP";

	/**
	 * 화면에 보여질 목록 갯수
	 */
	public final static Integer BOARD_MAX_LIMIT = 20;
	public final static Integer HOME_SIZE_LINE_NUMBER = 5;
	public final static Integer HOME_SIZE_POST = 7;
	public final static Integer HOME_SIZE_GALLERY = 10;
	public final static Integer RSS_SIZE_ITEM = 800;
	public final static Integer HOME_COMMENT_CONTENT_MAX_LENGTH = 110;
	public final static Integer SEARCH_CONTENT_MAX_LENGTH = 200;
	public final static Integer COMMENT_MAX_SIZE = 30;	
	public final static Integer GALLERY_SIZE = 24;
	public final static Integer BOARD_TOP_LIMIT = 3;

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
	public final static Integer COOKIE_EMAIL_MAX_AGE = 60 * 60 * 24 * 30; // 30 days
	
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

	// 게시물 감정 표현. 좋아요, 싫어요
	public enum FEELING_TYPE {
		LIKE,
		DISLIKE
	}

	// 게시물 감정 표현할때의 에러 코드
	public enum USER_FEELING_ERROR_CODE {
		ALREADY,		// 이미 감정 표현을 함
		WRITER			// 이 게시물의 작성자라서 감정 표현을 할 수 없음
	}

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
	
	/**
	 * 그림의 용량을 축소시키는 기준 값.
	 */
	public final static long GALLERY_MAXIUM_CAPACITY = 1048576; // Byte
	
	/**
	 * K리그 줄임말. 삭제 되어야 한다. Competition 클래스를 만들었다.
	 */
	public final static String K_LEAGUE_ABBREVIATION = "KL";
	public final static String K_LEAGUE_CLASSIC_ABBREVIATION = "KLCL";
	public final static String K_LEAGUE_CHALLENGE_ABBREVIATION = "KLCH";

	public final static int ELASTICSEARCH_BULK_LIMIT = 500;
	public final static String ELASTICSEARCH_TYPE_BOARD = "board";
	public final static String ELASTICSEARCH_TYPE_COMMENT = "comment";
	public final static String ELASTICSEARCH_TYPE_JAKDU_COMMENT = "jakduComment";
	public final static String ELASTICSEARCH_TYPE_GALLERY = "gallery";

	public enum NAME_TYPE {
		fullName,
		shortName
	}

	public enum CLUB_TYPE {
		FOOTBALL_CLUB,
		NATIONAL_TEAM
	}

	public enum CLUB_AGE {
		UNDER_14,
		UNDER_17,
		UNDER_20,
		UNDER_23,
		SENIOR
	}

	public enum CLUB_SEX {
		MEN,
		WOMEN
	}

	public enum JAKDU_GROUP_STATE {
		SCHEDULE,	// 경기 예정
		STANDBY,	// 대기중
		PLAYING,	// 진행중
		TIMEUP		// 경기 종료
	}

	public enum RESET_PASSWORD_RESULT {
		NONE,
		CODE_OK,
		SEND_OK,
		CHANGE_OK,
		INVALID
	}
}