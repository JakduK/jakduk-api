package com.jakduk;

import com.jakduk.model.db.BoardCategory;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.util.AbstractSpringTest;
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
	public void boardCategoryTest() {
		BoardCategory boardCategory = boardCategoryRepository.findByName("free");
		
		String[] usingBoard = boardCategory.getUsingBoard().toArray(new String[boardCategory.getUsingBoard().size()]);
		
		for (String tempUsingBoard : usingBoard) {
			System.out.println("boardCategoryTest=" + tempUsingBoard);
		}
	}
	
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
