package com.jakduk.api.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.configuration.CoreProperties;
import com.jakduk.core.service.BoardCategoryService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * Created by pyohwan on 17. 1. 4.
 */
public class BoardCategoryServiceTest extends CoreApplicationTests {

    @Autowired
    private BoardCategoryService sut;

    @Resource
    private CoreProperties coreProperties;

    @Ignore
    @Test
    public void initBoardCategory() {
        sut.initBoardCategory();
    }

}
