package com.jakduk.api.utils;

import com.jakduk.api.common.util.JakdukUtils;
import org.junit.Test;

public class JakdukUtilsTests {

    @Test
    public void generateTemporaryEmail() {
        System.out.println(JakdukUtils.generateTemporaryEmail());
    }

    @Test
    public void stripHtmlTag() {
        String sampleHtmlTag = "\uD83C\uDFC3\u200D♀️\uD83C\uDFC3\u200D♂️\uD83D\uDEB4\u200D♀️\uD83D\uDEB4\u200D♂️";

        System.out.println("origin string length : " + sampleHtmlTag.length());
        System.out.println("after parse length : " + JakdukUtils.stripHtmlTag(sampleHtmlTag).length());
    }
}
