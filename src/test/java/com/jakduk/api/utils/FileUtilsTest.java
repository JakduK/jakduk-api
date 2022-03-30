package com.jakduk.api.utils;

import com.jakduk.api.common.util.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by pyohwanjang on 2017. 2. 26..
 */
public class FileUtilsTest {

    @Test
    public void getBytesByUrl() throws IOException {
        FileUtils.FileInfo fileInfo = FileUtils.getBytesByUrl("https://avatars1.githubusercontent.com/u/19828371?v=4&s=200");

        assertTrue(Objects.nonNull(fileInfo));
    }

}
