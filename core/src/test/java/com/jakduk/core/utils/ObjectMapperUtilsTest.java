package com.jakduk.core.utils;

import com.jakduk.core.common.util.ObjectMapperUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 2. 25..
 */
public class ObjectMapperUtilsTest {

    @Test
    public void writeValueAsString() throws IOException {
        String json = "{\n" +
                "  \"about\": \"ì\u0095\u0088ë\u0085\u0095í\u0095\u0098ì\u0084¸ì\u009A\u0094.\",\n" +
                "  \"email\": \"phjang1983@daum.net\",\n" +
                "  \"externalPicture\": {\n" +
                "    \"externalLargePictureUrl\": \"aaa\",\n" +
                "    \"externalSmallPictureUrl\": \"bbb\"\n" +
                "  },\n" +
                "  \"username\": \"Jang,Pyohwan\"\n" +
                "}";

        ObjectMapperUtils.readValue(json, Map.class);
    }

}
