package com.jakduk.core.board;

import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.service.BoardCategoryService;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pyohwan on 17. 1. 4.
 */
public class BoardCategoryServiceTest extends AbstractSpringTest {

    @Autowired
    private BoardCategoryService sut;

    @Ignore
    @Test
    public void initBoardCategory() {
        sut.initBoardCategory();
    }

}
