package com.jakduk.api.search;

import com.jakduk.api.restcontroller.vo.search.PopularSearchWordResult;
import com.jakduk.api.service.SearchService;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */
public class SearchServiceTest {

	@Autowired
	private SearchService sut;

	@Disabled
	@Test
	public void searchUnified() throws IOException {
		sut.searchUnified("string", "ARTICLE;COMMENT;GALLERY", 0, 10, null, null);
	}

	@Disabled
	@Test
	public void aggregateSearchWord() throws IOException {
		// 한달전
		LocalDate oneMonthAgo = LocalDate.now().minusMonths(1L);
		PopularSearchWordResult result = sut.aggregateSearchWord(oneMonthAgo, 5);
		System.out.println("phjang=" + result);
	}

}
