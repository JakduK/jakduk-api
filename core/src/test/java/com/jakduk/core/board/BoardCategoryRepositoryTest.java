package com.jakduk.core.board;

import com.jakduk.core.CoreApplicationTests;
import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.BoardCategory;
import com.jakduk.core.repository.board.category.BoardCategoryRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by pyohwan on 17. 1. 25.
 */
public class BoardCategoryRepositoryTest extends CoreApplicationTests {

    @Autowired
    private BoardCategoryRepository sut;

    @Test
    public void findByLanguage() {
        List<BoardCategory> categories = sut.findByLanguage(CoreUtils.getLanguageCode());
    }

}
