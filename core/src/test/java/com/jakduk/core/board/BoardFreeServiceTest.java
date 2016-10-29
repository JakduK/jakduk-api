package com.jakduk.core.board;

import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.service.BoardFreeService;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pyohwan on 16. 10. 30.
 */
public class BoardFreeServiceTest extends AbstractSpringTest {

    @Autowired
    private BoardFreeService sut;

    @Test
    public void 자유게시판_말머리_목록() {
        List<BoardCategory> categories = sut.getFreeCategories();
        System.out.println("categories=" + categories.stream().collect(Collectors.toMap(BoardCategory::getCode, boardCategory -> boardCategory.getNames().get(0).getName())));
    }
}
