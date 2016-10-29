package com.jakduk.core.repository.board.category;

import com.jakduk.core.model.db.BoardCategory;

import java.util.List;

/**
 * Created by pyohwan on 16. 10. 30.
 */
public interface BoardCategoryCustom {

    /**
     * 해당 언어에 맞는 게시판 말머리 목록을 가져온다.
     * @param language 언어
     * @return 말머리 배열
     */
    List<BoardCategory> findByLanguage(String language);
}
