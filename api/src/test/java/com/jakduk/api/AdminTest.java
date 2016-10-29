package com.jakduk.api;

import com.jakduk.api.util.AbstractSpringTest;
import com.jakduk.core.repository.board.category.BoardCategoryRepository;
import com.jakduk.core.repository.board.free.BoardFreeRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 3.
 * @desc     :
 */

public class AdminTest extends AbstractSpringTest {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	BoardCategoryRepository boardCategoryRepository;
	
	@Test
	public void getBoard() {
//		long boardCnt = boardFreeRepository.count();
		long boardCnt = 101;
		int limit = 100;
		int totalPage = (int)(boardCnt / limit);
		
		System.out.println("boardCnt=" + boardCnt);
		System.out.println("totalPage=" + totalPage);
	}

}
