package com.jakduk.api.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.model.embedded.LocalSimpleName;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class BoardCategoryTests {

    @Test
    public void test01() {

        List<BoardCategory> categories =  BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, Locale.US);

        categories.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories2 =  BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.FOOTBALL, Locale.KOREA);

        categories2.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories3 =  BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.DEVELOPER, Locale.US);

        categories3.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories4 =  BoardCategoryGenerator.getCategories(Constants.BOARD_TYPE.DEVELOPER, Locale.KOREA);

        categories4.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        BoardCategory boardCategory = new BoardCategory() {{
            setCode("KL1");
            setNames(Arrays.asList(new LocalSimpleName("ko", "K리그 1")));
        }};

        Assert.assertEquals(boardCategory.toString(), BoardCategoryGenerator.getCategory(Constants.BOARD_TYPE.FOOTBALL, BoardCategoryGenerator.Category.KL1.name(), Locale.KOREA).toString());

    }


}
