package com.jakduk.api.board;

import com.jakduk.api.ApiApplicationTests;

import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.repository.board.category.BoardCategoryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pyohwan on 17. 1. 25.
 */
public class BoardCategoryRepositoryTest extends ApiApplicationTests {

    @Autowired
    private BoardCategoryRepository sut;

    @Test
    public void findByLanguage() {
        List<BoardCategory> categories = sut.findByLanguage(JakdukUtils.getLanguageCode());
    }

}
