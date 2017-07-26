package com.jakduk.api.utils;

import com.jakduk.api.common.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * Created by pyohwanjang on 2017. 2. 26..
 */
public class FileUtilsTest {

    @Test
    public void getBytesByUrl() throws IOException {
        FileUtils.FileInfo fileInfo = FileUtils.getBytesByUrl("https://api.jakduk.com/gallery/58ba9ebb1033ba2b775b215c");

        Assert.assertTrue(Objects.nonNull(fileInfo));
    }

}
