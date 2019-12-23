package com.jakduk.api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jakduk.api.common.util.ObjectMapperUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /*
    실패 케이스 있음

    "2017-09-24T16:45:47.640"
    "2017-09-24T16:45:47.64"
     */
    @Disabled
    @Test
    public void iso8601Test() throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.now();

        String expect = "\"" + localDateTime.toString() + "\"";

        System.out.println(expect);
        System.out.println(ObjectMapperUtils.writeValueAsString(localDateTime));

        assertTrue(expect.equals(ObjectMapperUtils.writeValueAsString(localDateTime)));
    }

}
