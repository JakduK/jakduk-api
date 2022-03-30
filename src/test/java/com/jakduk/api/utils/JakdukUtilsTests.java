package com.jakduk.api.utils;

import com.jakduk.api.common.util.JakdukUtils;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JakdukUtilsTests {

	@Test
	public void generateTemporaryEmail() {
		System.out.println(JakdukUtils.generateTemporaryEmail());
	}

	@Test
	public void stripHtmlTag() {
		String emojiTag = "<p>\uD83C\uDFC3\u200D♀️\uD83C\uDFC3\u200D♂️\uD83D\uDEB4\u200D♀️\uD83D\uDEB4\u200D♂️</p>";
		String imgTag = "<img src=\"smiley.gif\" alt=\"Smiley face\" height=\"42\" width=\"42\">";
		String aTag = "<a href=\"https://www.w3schools.com\">Visit W3Schools.com!</a>";

		System.out.println("");
		assertEquals(27, emojiTag.length());
		assertEquals(20, JakdukUtils.stripHtmlTag(emojiTag).length());
		assertEquals(StringUtils.EMPTY.length(), JakdukUtils.stripHtmlTag(imgTag).length());
		assertEquals("Visit W3Schools.com!", JakdukUtils.stripHtmlTag(aTag));
	}
}
