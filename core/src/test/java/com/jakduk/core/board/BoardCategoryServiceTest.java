package com.jakduk.core.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.service.BoardCategoryService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by pyohwan on 17. 1. 4.
 */
public class BoardCategoryServiceTest extends CoreApplicationTests {

    @Autowired
    private BoardCategoryService sut;

    @Ignore
    @Test
    public void initBoardCategory() {
        sut.initBoardCategory();
    }

}
