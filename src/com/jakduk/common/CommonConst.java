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
	public final static Integer BOARD_NAME_FREE = 10;
	
	public final static Integer SHORT_HISTORY_EN = 50;
	public final static Integer SHORT_HISTORY_KO = 51;
	
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
	public final static Integer BOARD_CATEGORY_NONE = 10;
	public final static Integer BOARD_CATEGORY_FREE = 11;
	public final static Integer BOARD_CATEGORY_FOOTBALL = 12;
	public final static Integer BOARD_CATEGORY_DEVELOP = 13;
	public final static Integer BOARD_CATEGORY_ALL = 14;
	
	/**
	 * 언어. 
	 */
	public final static Integer LANGUAGE_EN = 10;
	public final static Integer LANGUAGE_KO = 11;
	
	public final static String COOKIE_EMAIL = "email";
	public final static String COOKIE_REMEMBER = "remember";
	
	public final static Integer OAUTH_TYPE_FACEBOOK = 10;
	public final static Integer OAUTH_TYPE_DAUM = 11;
	
}
