package com.jakduk.core.search;

import com.jakduk.core.service.SearchService;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 2.
 */
public class SearchServiceTest extends AbstractSpringTest {

	@Autowired
	private SearchService sut;

	@Test
	public void searchUnified() {
		sut.searchUnified("string", "PO;CO;GA", 0, 10);
	}

	@Test
	public void aggregateSearchWord() {
		// 한달전
		Long registerDateFrom = LocalDate.now().minusMonths(1L).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
		sut.aggregateSearchWord(registerDateFrom, 5);
	}
}
