package com.jakduk.core.search;

import com.jakduk.core.service.SearchService;
import com.jakduk.core.util.AbstractSpringTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

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
}
