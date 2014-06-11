package com.jakduk.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardSequence;
import com.jakduk.model.web.BoardPageInfo;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     : 공통으로 쓰이는 서비스
 */

@Service
public class CommonService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * 게시판의 차기 글번호를 가져온다.
	 * @param name 게시판 ID
	 * @return 다음 글번호
	 */
	public Integer getNextSequence(Integer name) {
		
		Integer nextSeq = -1;
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.inc("seq", 1);
		
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		
		BoardSequence board = mongoTemplate.findAndModify(query, update, options, BoardSequence.class);
		
		if (board == null) {
			logger.debug("err result=" + board);
			return nextSeq;
		} else {
			nextSeq = board.getSeq();
			return nextSeq;
		}
	}

	/**
	 * 게시판 목록의 페이지에 대한 정보를 넘겨준다.
	 * @param page 현재 페이지 번호
	 * @param numberPosts 전체 게시물 갯수
	 * @param numberPages 전체 페이지 수
	 * @return BoardPageInfo 객체
	 */
	public BoardPageInfo getCountPages(Long page, Long numberPosts, Integer numberPages) {
		
		BoardPageInfo boardPageInfo = new BoardPageInfo();

		Long totalPages = numberPosts / CommonConst.BOARD_LINE_NUMBER;
		Long prevPage = (long) 1;
		Long nextPage = (long) 1;
		
		if ((totalPages % CommonConst.BOARD_LINE_NUMBER) > 0 || (numberPosts - CommonConst.BOARD_LINE_NUMBER) <= 0) {
			totalPages++; 
		} else if ((totalPages % CommonConst.BOARD_LINE_NUMBER) == 0) {
			totalPages--;
		}
		
		Long startPage = page - (page % numberPages) + 1;
		
		if (startPage <= 0) {
			startPage ++;
		}
		
		if (startPage > numberPages) {
			prevPage = startPage - numberPages;
		} else {
			prevPage = (long) -1;
		}
		
		Long endPage = (long) 1;
		
		if ((totalPages - page) > numberPages) {
			endPage = startPage + numberPages - 1;
			nextPage = startPage + numberPages;
		} else {
			endPage = totalPages;
			nextPage = (long) -1;
		}
		
		boardPageInfo.setStartPage(startPage);
		boardPageInfo.setEndPage(endPage);
		boardPageInfo.setPrevPage(prevPage);
		boardPageInfo.setNextPage(nextPage);
		
		return boardPageInfo;
	}
}
