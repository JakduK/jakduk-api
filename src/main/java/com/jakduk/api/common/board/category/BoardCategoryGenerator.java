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
        CLASSIC(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "Classic"), new LocalSimpleName(Locale.KOREA.getLanguage(), "클래식")),
        CHALLENGE(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "Challenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "챌린지")),
        NATIONAL_LEAGUE(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "National League"), new LocalSimpleName(Locale.KOREA.getLanguage(), "내셔널리그")),
        K3(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "K3"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K3")),
        WK(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "WK"), new LocalSimpleName(Locale.KOREA.getLanguage(), "WK")),
        NATIONAL_TEAM(Constants.BOARD_TYPE.FOOTBALL, new LocalSimpleName(Locale.US.getLanguage(), "National Team"), new LocalSimpleName(Locale.KOREA.getLanguage(), "국가대표팀")),

		QUESTION(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Question"), new LocalSimpleName(Locale.KOREA.getLanguage(), "물음")),
		JOBS(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Jobs"), new LocalSimpleName(Locale.KOREA.getLanguage(), "일자리")),
		CODE_CHALLENGE(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Code challenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "코드 챌린지")),
		IDEA(Constants.BOARD_TYPE.DEVELOPER, new LocalSimpleName(Locale.US.getLanguage(), "Idea"), new LocalSimpleName(Locale.KOREA.getLanguage(), "아이디어")),
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
				.map(category -> BoardCategory.builder()
						.code(category.name())
						.names(category.names
								.stream()
								.filter(localSimpleName -> locale.getLanguage().equals(localSimpleName.getLanguage()))
								.collect(Collectors.toList()))
						.build())
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
					.map(category -> BoardCategory.builder()
							.code(category.name())
							.names(category.names
									.stream()
									.filter(localSimpleName -> locale.getLanguage().equals(localSimpleName.getLanguage()))
									.collect(Collectors.toList()))
							.build()
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
    		return Category.list(boardType)
					.stream()
					.anyMatch(category -> category.equals(targetCategory));
		}
    }

}
