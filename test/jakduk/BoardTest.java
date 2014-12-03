package jakduk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.embedded.BoardUser;
import com.jakduk.model.simple.BoardFreeOnComment;
import com.jakduk.repository.BoardFreeRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class BoardTest {
	
	@Autowired
	BoardFreeRepository boardFreeRepository;
	
	@Test
	public void test01() {
		BoardFree boardFree = boardFreeRepository.findOneBySeq(11);
		
	}
	
	@Test
	public void test02() {
		System.out.println(boardFreeRepository.findOne("5460cfe9e4b06faf36d26efc"));
		
		List<String> usingBoard = new ArrayList<String>();
		usingBoard.add(CommonConst.BOARD_NAME_FREE);
		System.out.println("usingBoard=" + usingBoard);
		
		usingBoard = Collections.emptyList();
		System.out.println("usingBoard=" + usingBoard);
//		usingBoard.add(CommonConst.BOARD_NAME_FREE);
		System.out.println("usingBoard=" + usingBoard);
	}
	
	@Test
	public void test03() {
		
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(22);
		System.out.println("boardFreeOnComment=" + boardFreeOnComment);
		
	}
	
}
