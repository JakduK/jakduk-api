package com.jakduk.api.board;

import com.jakduk.api.common.BoardCategory;
import org.junit.Test;

public class BoardCategoryTests {

    @Test
    public void test01() {
        System.out.println("phjang=" + BoardCategory.FOOTBALL.CLASSIC.getNames().get(0).getLanguage());
    }
}
