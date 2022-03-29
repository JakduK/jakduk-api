package com.jakduk.api.common;

import org.springframework.data.domain.Sort;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public class Constants {

	/**
	 * 게시판 종류.
	 */
	public enum BOARD_TYPE {
		FREE,
		FOOTBALL,
		DEVELOPER
	}

	/**
	 * 백과사전 언어 구분.
	 */
	public final static String ENCYCLOPEDIA_EN = "encyclopediaEn";
	public final static String ENCYCLOPEDIA_KO = "encyclopediaKo";

	public final static String SEQ_BOARD = "BOARD";
	public final static String SEQ_JAKDU_SCHEDULE_GROUP = "JAKDU_SCHEDULE_GROUP";

	/**
	 * 화면에 보여질 목록 갯수
	 */
	public final static Integer BOARD_MAX_LIMIT = 20;
	public final static Integer HOME_SIZE_LINE_NUMBER = 5;
	public final static Integer HOME_SIZE_POST = 7;
	public final static Integer HOME_SIZE_GALLERY = 10;
	public final static Integer HOME_COMMENT_CONTENT_MAX_LENGTH = 110;
	public final static Integer SEARCH_NO_MATCH_SIZE = 200;
	public final static Integer SEARCH_FRAGMENT_SIZE = 200;
	public final static Integer COMMENT_MAX_LIMIT = 30;
	public final static Integer GALLERY_SIZE = 24;
	public final static Integer BOARD_TOP_LIMIT = 3;
	public final static Integer ARTICLE_SHORT_CONTENT_LENGTH = 100;

	/**
	 * Social 프로바이더 종류.
	 */
	public enum ACCOUNT_TYPE {
		DAUM, // deprecated
		JAKDUK,
		FACEBOOK,
		KAKAO,
		NAVER,
		GOOGLE
	}

	// 게시물 감정 표현. 좋아요, 싫어요
	public enum FEELING_TYPE {
		LIKE,
		DISLIKE
	}

	/**
	 * 글 지움 종류
	 */
	public enum ARTICLE_DELETE_TYPE {
		CONTENT,	// 글 본문만 지움(댓글 유지)
		ALL			// 모두 지움
	}

    /**
     * 사진을 등록한 출처
     */
    public enum GALLERY_FROM_TYPE {
		ARTICLE,
		ARTICLE_COMMENT
    }

	/**
	 * 사진 가로, 세로 길이
	 */
	public final static Integer GALLERY_THUMBNAIL_SIZE_WIDTH = 360;
	public final static Integer GALLERY_THUMBNAIL_SIZE_HEIGHT = 230;
	public final static Integer USER_SMALL_PICTURE_SIZE_WIDTH = 50;
	public final static Integer USER_SMALL_PICTURE_SIZE_HEIGHT = 50;

	/**
	 * 사진 상태값.
	 */
	public enum GALLERY_STATUS_TYPE {
		TEMP,
		ENABLE,
	}

	/**
	 * 그림의 용량을 축소시키는 기준 값.
	 */
	public final static long GALLERY_MAXIMUM_CAPACITY = 1048576; // Byte 단위. 현재 1MB.
	
	/**
	 * K리그 줄임말. 삭제 되어야 한다. Competition 클래스를 만들었다.
	 */
	public final static String K_LEAGUE_ABBREVIATION = "KL";

	public final static String ES_TYPE_ARTICLE = "article";
	public final static String ES_TYPE_COMMENT = "comment";
	public final static String ES_TYPE_GALLERY = "gallery";
	public final static String ES_TYPE_SEARCH_WORD = "search_word";


	// 이름 타입.
	public enum NAME_TYPE {
		fullName,
		shortName
	}

	// 축구단 성격.
	public enum CLUB_TYPE {
		FOOTBALL_CLUB,
		NATIONAL_TEAM
	}

	// 축구단 연령대.
	public enum CLUB_AGE {
		UNDER_14,
		UNDER_17,
		UNDER_20,
		UNDER_23,
		SENIOR
	}

	// 축구단 성별.
	public enum CLUB_SEX {
		MEN,
		WOMEN
	}

	// 작두 경기의 상태.
	public enum JAKDU_GROUP_STATE {
		SCHEDULE,	// 경기 예정
		STANDBY,	// 대기중
		PLAYING,	// 진행중
		TIMEUP		// 경기 종료
	}

	// 토큰 타입
	public enum TOKEN_TYPE {
		RESET_PASSWORD
	}

	// 이미지 타입
	public enum IMAGE_TYPE {
		FULL,		// 전체크기
		THUMBNAIL	// 썸네일
	}

	public enum SEARCH_INCLUDE_TYPE {
		ARTICLE,	// 게시물
		COMMENT,	// 댓글
		GALLERY    // 사진첩
	}

	public enum IMAGE_SIZE_TYPE {
		LARGE,
		SMALL
	}

	/**
	 * 몽고DB Criteria Operator
	 */
	public enum CRITERIA_OPERATOR {
		GT,
		LT
	}

	/**
	 * mongoDB collection 이름
	 */
	public final static String COLLECTION_USER = "user";
	public final static String COLLECTION_ARTICLE = "article";
	public final static String COLLECTION_ARTICLE_COMMENT = "articleComment";
	public final static String COLLECTION_GALLERY = "gallery";

	public enum EMAIL_TYPE {
		WELCOME,
		RESET_PASSWORD,
		BULK
	}

	/**
	 * 읽음 수의 쿠키 만료 시간(초)
	 */
	public final static Integer VIEWS_COOKIE_EXPIRE_SECONDS = 30;

	/**
	 * RSS, Sitemap 에서 한번에 읽을 아이템 수
	 */
	public final static Integer NUMBER_OF_ITEMS_EACH_PAGES = 1000;

	/**
	 * 사진 상세에서 해당 사진의 앞뒤 사진의 갯수
	 */
	public final static Integer NUMBER_OF_ITEMS_IN_SURROUNDINGS_GALLERY=10;

	/**
	 * 읽음 중복 방지를 위한 쿠키
	 */
	public enum VIEWS_COOKIE_TYPE {
		ARTICLE
	}

	/**
	 * SNS 가입시 임시로 저장할 프로필를 담을 세션
	 * 참고 {@link org.springframework.social.connect.web.ProviderSignInAttempt#SESSION_ATTRIBUTE}
	 */
	public final static String PROVIDER_SIGNIN_ATTEMPT_SESSION_ATTRIBUTE = "ProviderSignInAttempt";

	/**
	 * 글 로그 상태
	 */
	public enum ARTICLE_LOG_TYPE {
		CREATE,
		EDIT,
		DELETE,
		ENABLE_NOTICE,
		DISABLE_NOTICE
	}

	/**
	 * 댓글 로그 상태
	 */
	public enum ARTICLE_COMMENT_LOG_TYPE {
		CREATE,
		EDIT
	}

	public static Sort SORT_BY_ID_DESC = Sort.by(Sort.Direction.DESC, "_id");
	public static Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "_id");

}
