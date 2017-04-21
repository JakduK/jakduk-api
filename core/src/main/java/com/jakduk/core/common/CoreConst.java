package com.jakduk.core.common;
/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 4. 29.
 * @desc     :
 */
public class CoreConst {

	/**
	 * 게시판 종류.
	 */
	public enum BOARD_TYPE {
		BOARD_FREE
	}

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
	public final static Integer HOME_COMMENT_CONTENT_MAX_LENGTH = 110;
	public final static Integer SEARCH_NO_MATCH_SIZE = 200;
	public final static Integer SEARCH_FRAGMENT_SIZE = 200;
	public final static Integer COMMENT_MAX_LIMIT = 30;
	public final static Integer GALLERY_SIZE = 24;
	public final static Integer BOARD_TOP_LIMIT = 3;

	/**
	 * 게시판 말머리.
	 */
	public final static String BOARD_CATEGORY_NONE = "none";
	public final static String BOARD_CATEGORY_FREE = "free";
	public final static String BOARD_CATEGORY_FOOTBALL = "football";
	public final static String BOARD_CATEGORY_DEVELOP = "develop";
	public final static String BOARD_CATEGORY_ALL = "all";

	public final static Integer BOARD_SHORT_CONTENT_LENGTH = 100;

	/**
	 * 게시판 말머리 종류.
	 */
	public enum BOARD_CATEGORY_TYPE {
		ALL,		// 전체
		FREE,		// 자유
		FOOTBALL	// 축구
	}

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
	 * Social 프로바이더 종류.
	 */
	public enum ACCOUNT_TYPE {
		JAKDUK,
		FACEBOOK,
		DAUM
	}

	// 게시물 감정 표현. 좋아요, 싫어요
	public enum FEELING_TYPE {
		LIKE,
		DISLIKE
	}

	/**
	 * 글 지움 종류
	 */
	public enum BOARD_DELETE_TYPE {
		CONTENT,	// 글 본문만 지움(댓글 유지)
		ALL			// 모두 지움
	}

	/**
	 * 글 히스토리 상태
	 */
	public enum BOARD_HISTORY_TYPE {
		CREATE,
		EDIT,
		DELETE,
		ENABLE_NOTICE,
		DISABLE_NOTICE
	}

    /**
     * 사진을 등록한 출처
     */
    public enum GALLERY_FROM_TYPE {
        BOARD_FREE,
		BOARD_FREE_COMMENT
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
	 * 디바이스 타입
     */
	public enum DEVICE_TYPE {
		NORMAL,	// PC
		MOBILE,	// 모바일
		TABLET	// 태블릿
	}
	
	/**
	 * 그림의 용량을 축소시키는 기준 값.
	 */
	public final static long GALLERY_MAXIMUM_CAPACITY = 1048576; // Byte 단위. 현재 1MB.
	
	/**
	 * K리그 줄임말. 삭제 되어야 한다. Competition 클래스를 만들었다.
	 */
	public final static String K_LEAGUE_ABBREVIATION = "KL";
	public final static String K_LEAGUE_CLASSIC_ABBREVIATION = "KLCL";
	public final static String K_LEAGUE_CHALLENGE_ABBREVIATION = "KLCH";

	public final static Integer ES_BULK_LIMIT = 1000;
	public final static Integer ES_AWAIT_CLOSE_TIMEOUT_MINUTES = 2;
	public final static String ES_TYPE_BOARD = "board";
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

	// 배치 타입
	public enum BATCH_TYPE {
		CHANGE_BOARD_CONTENT_IMAGE_URL_01,
		APPEND_GALLERY_FILE_EXT_01,
		BOARD_FREE_ADD_SHORT_CONTENT_01,			// 본문 미리보기 용으로, HTML이 제거된 100자 정도의 본문 요약 필드가 필요하다
		BOARD_FREE_ADD_LAST_UPDATED_01,				// BoardFree에 lastUpdated 필드를 추가한다.
		BOARD_FREE_ADD_LINKED_GALLERY_01,			// BoardFree에 linkedGallery 필드를 추가한다.
		GALLERY_CHANGE_POSTS_TO_LINKED_ITEMS_01,	// Gallery의 posts를 linkedItems으로 바꾼다.
		GALLERY_ADD_HASH_FIELD_01,					// Gallery에 hash 필드 추가.
		GALLERY_CHECK_NAME_01						// Gallery 의 name이 fileName과 동일하면 ""로 엎어친다.
	}

	public enum SEARCH_TYPE {
		PO,	// 게시물
		CO,	// 댓글
		GA	// 사진첩
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
	public final static String COLLECTION_BOARD_FREE = "boardFree";
	public final static String COLLECTION_GALLERY = "gallery";

}
