package com.jakduk.core.service;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.model.embedded.LocalSimpleName;
import com.jakduk.core.repository.board.category.BoardCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author pyohwan
 *         16. 7. 19 오후 9:35
 */

@Slf4j
@Service
public class BoardCategoryService {

    @Autowired
    private BoardCategoryRepository boardCategoryRepository;

    /**
     * 자유게시판 말머리 목록
     *
     * @return 말머리 목록
     */
    public List<BoardCategory> getFreeCategories() {
        return boardCategoryRepository.findByLanguage(CoreUtils.getLanguageCode(LocaleContextHolder.getLocale(), null));
    }

    /**
     * 자유게시판 말머리 가져오기
     */
    public BoardCategory getFreeCategory(String code) {
        return boardCategoryRepository.findByCodeAndLanguage(code, CoreUtils.getLanguageCode(LocaleContextHolder.getLocale(), null));
    }

    /**
     * 게시판 카테고리 초기화.
     */
    public void initBoardCategory() {

        boardCategoryRepository.deleteAll();

        List<BoardCategory> boardCategories = new ArrayList<>();

        boardCategories.add(
                BoardCategory.builder()
                        .code("FREE")
                        .names(
                                new ArrayList<LocalSimpleName>() {
                                    {
                                        add(new LocalSimpleName(Locale.KOREAN.getLanguage(), "자유"));
                                        add(new LocalSimpleName(Locale.ENGLISH.getLanguage(), "FREE"));
                                    }
                                })
                        .build()
        );

        boardCategories.add(
                BoardCategory.builder()
                        .code("FOOTBALL")
                        .names(
                                new ArrayList<LocalSimpleName>() {
                                    {
                                        add(new LocalSimpleName(Locale.KOREAN.getLanguage(), "축구"));
                                        add(new LocalSimpleName(Locale.ENGLISH.getLanguage(), "FOOTBALL"));
                                    }
                                })
                        .build()
        );

        boardCategoryRepository.save(boardCategories);

        log.debug("inital board category.");
    }
}
