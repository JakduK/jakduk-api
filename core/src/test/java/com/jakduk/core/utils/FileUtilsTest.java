package com.jakduk.core.utils;

import com.jakduk.core.common.util.FileUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by pyohwanjang on 2017. 2. 26..
 */
public class FileUtilsTest {

    @Test
    public void getBytesByUrl() throws IOException {
        FileUtils.getBytesByUrl("https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876");
    }
}
