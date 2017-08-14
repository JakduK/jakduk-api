package com.jakduk.api.board;

import com.jakduk.api.common.board.category.BoardCategoryGenerator;
import com.jakduk.api.common.Constants;
import com.jakduk.api.common.board.category.BoardCategory;
import org.junit.Test;

import java.util.List;
import java.util.Locale;

public class BoardCategoryTests {

    @Test
    public void test01() {

        List<BoardCategory> categories =  new BoardCategoryGenerator().getCategories(Constants.BOARD_TYPE.FOOTBALL, Locale.US);

        categories.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories2 =  new BoardCategoryGenerator().getCategories(Constants.BOARD_TYPE.FOOTBALL, Locale.KOREA);

        categories2.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories3 =  new BoardCategoryGenerator().getCategories(Constants.BOARD_TYPE.DEVELOPER, Locale.US);

        categories3.forEach(boardCategory -> System.out.println(boardCategory.getNames()));

        List<BoardCategory> categories4 =  new BoardCategoryGenerator().getCategories(Constants.BOARD_TYPE.DEVELOPER, Locale.KOREA);

        categories4.forEach(boardCategory -> System.out.println(boardCategory.getNames()));


        System.out.println(new BoardCategoryGenerator().getCategory(Constants.BOARD_TYPE.FOOTBALL, "CLASSIC", Locale.KOREA));

    }


}
