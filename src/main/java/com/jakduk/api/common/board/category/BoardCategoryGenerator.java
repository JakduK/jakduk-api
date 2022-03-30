package com.jakduk.api.common.board.category;

import com.jakduk.api.common.Constants;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.embedded.LocalSimpleName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BoardCategoryGenerator {

	private BoardCategoryGenerator() {}

    public enum Category {
        KL1(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "K LEAGUE 1"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K리그 1")),
        KL2(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "K LEAGUE 2"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K리그 2")),
		ACL(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "ACL"), new LocalSimpleName(Locale.KOREA.getLanguage(), "ACL")),
		NATIONAL_TEAM(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "National Team"), new LocalSimpleName(Locale.KOREA.getLanguage(), "국가대표팀")),
		OVERSEAS_FOOTBALL(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "Overseas Football"), new LocalSimpleName(Locale.KOREA.getLanguage(), "해외축구")),
        NATIONAL_LEAGUE(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "National League"), new LocalSimpleName(Locale.KOREA.getLanguage(), "내셔널리그")),
        K3(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "K3"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K3")),
        WK(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "WK"), new LocalSimpleName(Locale.KOREA.getLanguage(), "WK")),
		AMATEUR(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "Amateur"), new LocalSimpleName(Locale.KOREA.getLanguage(), "아마추어")),
		ETC(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "ETC"), new LocalSimpleName(Locale.KOREA.getLanguage(), "기타")),

		QUESTION(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Question"), new LocalSimpleName(Locale.KOREA.getLanguage(), "궁금")),
		UPDATE(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Update"), new LocalSimpleName(Locale.KOREA.getLanguage(), "업데이트")),
		TALKING(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Talking"), new LocalSimpleName(Locale.KOREA.getLanguage(), "사는얘기")),
		TIP(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Tip"), new LocalSimpleName(Locale.KOREA.getLanguage(), "팁"));

		private static List<Category> FREE = Collections.emptyList();
		private static List<Category> FOOTBALL = Stream.of(Category.values()).filter(category -> Constants.BOARD_TYPE.FOOTBALL.equals(category.board)).collect(Collectors.toList());
		private static List<Category> DEVELOPER = Stream.of(Category.values()).filter(category -> Constants.BOARD_TYPE.DEVELOPER.equals(category.board)).collect(Collectors.toList());

        private Constants.BOARD_TYPE board;
		private List<LocalSimpleName> names;

        Category(Constants.BOARD_TYPE board, LocalSimpleName... names) {
        	this.board = board;
			this.names = Arrays.asList(names);
        }

        static List<Category> list(Constants.BOARD_TYPE board) {
			switch (board) {
				case FOOTBALL:
					return FOOTBALL;
				case DEVELOPER:
					return DEVELOPER;
				default:
					return FREE;
			}
		}
    }

    /**
     * 해당 언어에 맞는 게시판 말머리 목록을 가져온다.
     */
    public static List<BoardCategory> getCategories(Constants.BOARD_TYPE boardType, Locale locale) {
		return Category.list(boardType)
				.stream()
				.map(category -> new BoardCategory() {{
							setCode(category.name());
							setNames(
									category.names.stream()
											.filter(localSimpleName -> locale.getLanguage().equals(localSimpleName.getLanguage()))
											.collect(Collectors.toList()));
						}}
				)
				.collect(Collectors.toList());
	}

    /**
     * 해당 언어에 맞는 게시판 말머리 하나를 가져온다.
     */
    public static BoardCategory getCategory(Constants.BOARD_TYPE boardType, String targetCategoryName, Locale locale) {
		if (Constants.BOARD_TYPE.FREE.equals(boardType)) {
			return null;
		} else {
			Category targetCategory = Category.valueOf(targetCategoryName);
			return Category.list(boardType)
					.stream()
					.filter(category -> category.equals(targetCategory))
					.findFirst()
					.map(category -> new BoardCategory() {{
								setCode(category.name());
								setNames(
										category.names.stream()
												.filter(localSimpleName -> locale.getLanguage().equals(localSimpleName.getLanguage()))
												.collect(Collectors.toList()));
							}}
					)
					.orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));
		}
    }

    /**
     * 해당 말머리가 존재 하는지 확인 한다.
     */
    public static Boolean existCategory(Constants.BOARD_TYPE boardType, String targetCategoryName) {
    	if (Constants.BOARD_TYPE.FREE.equals(boardType)) {
    		return Boolean.TRUE;
		} else {
    		Category targetCategory = Category.valueOf(targetCategoryName);
			return Category.list(boardType).stream()
					.anyMatch(category -> category.equals(targetCategory));
		}
    }

	/**
	 * 해당 말머리가 존재 하지 않는지 확인 한다.
	 */
	public static Boolean notExistCategory(Constants.BOARD_TYPE boardType, String targetCategoryName) {

		return ! BoardCategoryGenerator.existCategory(boardType, targetCategoryName);
	}

}
