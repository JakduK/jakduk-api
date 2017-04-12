package com.jakduk.core.utils;

import com.jakduk.core.common.util.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
