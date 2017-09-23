package com.jakduk.api.common.board.category;

import com.jakduk.api.common.Constants;
import com.jakduk.api.exception.ServiceError;
import com.jakduk.api.exception.ServiceException;
import com.jakduk.api.model.embedded.LocalSimpleName;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BoardCategoryGenerator {

    private List<BoardCategory> footballCategories = Arrays.asList(
            BoardCategory.builder()
                    .code("CLASSIC")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Classic"), new LocalSimpleName(Locale.KOREA.getLanguage(), "클래식")))
                    .build(),
            BoardCategory.builder()
                    .code("CHALLENGE")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Challenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "챌린지")))
                    .build(),
            BoardCategory.builder()
                    .code("NATIONAL_LEAGUE")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "National league"), new LocalSimpleName(Locale.KOREA.getLanguage(), "내셔널리그")))
                    .build(),
            BoardCategory.builder()
                    .code("K3")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "K3"), new LocalSimpleName(Locale.KOREA.getLanguage(), "K3")))
                    .build(),
            BoardCategory.builder()
                    .code("WK")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "WK"), new LocalSimpleName(Locale.KOREA.getLanguage(), "WK")))
                    .build(),
            BoardCategory.builder()
                    .code("NATIONAL_TEAM")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "National team"), new LocalSimpleName(Locale.KOREA.getLanguage(), "국가대표팀")))
                    .build()
    );

    private List<BoardCategory> developerCategories = Arrays.asList(
            BoardCategory.builder()
                    .code("QUESTION")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Question"), new LocalSimpleName(Locale.KOREA.getLanguage(), "물음")))
                    .build(),
            // by siverprize
            BoardCategory.builder()
                    .code("JOBS")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Jobs"), new LocalSimpleName(Locale.KOREA.getLanguage(), "일자리")))
                    .build(),
            // 특정문제에 대해서 가장 작은 바이트로 코딩하면 우승 by fxpark
            BoardCategory.builder()
                    .code("CODE_CHALLENGE")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Code challenge"), new LocalSimpleName(Locale.KOREA.getLanguage(), "코드 챌린지")))
                    .build(),
            // by nari
            BoardCategory.builder()
                    .code("IDEA")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Idea"), new LocalSimpleName(Locale.KOREA.getLanguage(), "아이디어")))
                    .build(),
            // by nari
            BoardCategory.builder()
                    .code("TIP")
                    .names(Arrays.asList(new LocalSimpleName(Locale.US.getLanguage(), "Tip"), new LocalSimpleName(Locale.KOREA.getLanguage(), "팁")))
                    .build()
    );

    /**
     * 해당 언어에 맞는 게시판 말머리 목록을 가져온다.
     */
    public List<BoardCategory> getCategories(Constants.BOARD_TYPE boardType, Locale locale) {

        Consumer<BoardCategory> localeNamesConsumer = boardCategory -> {
            List<LocalSimpleName> names = boardCategory.getNames().stream()
                    .filter(localSimpleName -> localSimpleName.getLanguage().equals(locale.getLanguage()))
                    .collect(Collectors.toList());
            boardCategory.setNames(names);
        };

        switch (boardType) {
            case FOOTBALL:
                return footballCategories.stream()
                        .peek(localeNamesConsumer)
                        .collect(Collectors.toList());
            case DEVELOPER:
                return developerCategories.stream()
                        .peek(localeNamesConsumer)
                        .collect(Collectors.toList());
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 해당 언어에 맞는 게시판 말머리 하나를 가져온다.
     */
    public BoardCategory getCategory(Constants.BOARD_TYPE boardType, String code, Locale locale) {

        Consumer<BoardCategory> localeNamesConsumer = boardCategory -> {
            List<LocalSimpleName> names = boardCategory.getNames().stream()
                    .filter(localSimpleName -> localSimpleName.getLanguage().equals(locale.getLanguage()))
                    .collect(Collectors.toList());
            boardCategory.setNames(names);
        };

        switch (boardType) {
            case FOOTBALL:
                return footballCategories.stream()
                        .filter(boardCategory -> boardCategory.getCode().equals(code))
                        .peek(localeNamesConsumer)
                        .findFirst()
                        .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));
            case DEVELOPER:
                return developerCategories.stream()
                        .filter(boardCategory -> boardCategory.getCode().equals(code))
                        .peek(localeNamesConsumer)
                        .findFirst()
                        .orElseThrow(() -> new ServiceException(ServiceError.NOT_FOUND));
            default:
                return null;
        }

    }

    /**
     * 해당 말머리가 존재 하는지 확인 한다.
     */
    public Boolean existCategory(Constants.BOARD_TYPE boardType, String code) {
        switch (boardType) {
            case FOOTBALL:
                return footballCategories.stream()
                        .anyMatch(boardCategory -> boardCategory.getCode().equals(code));
            case DEVELOPER:
                return developerCategories.stream()
                        .anyMatch(boardCategory -> boardCategory.getCode().equals(code));
            default:
                return Boolean.FALSE;
        }
    }

}
