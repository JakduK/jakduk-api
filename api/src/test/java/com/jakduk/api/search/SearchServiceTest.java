package com.jakduk.api.search;

import com.jakduk.api.ApiApplicationTests;
import com.jakduk.api.service.SearchService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */
public class SearchServiceTest extends ApiApplicationTests {

	@Autowired
	private SearchService sut;

	@Test
	public void searchUnified() {
		sut.searchUnified("string", "PO;CO;GA", 0, 10, null, null);
	}

	@Test
	public void aggregateSearchWord() {
		// 한달전
		Long registerDateFrom = LocalDate.now().minusMonths(1L).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		sut.aggregateSearchWord(registerDateFrom, 5);
	}
}
