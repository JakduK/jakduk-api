package com.jakduk.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.web.BoardPageInfo;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 24.
 * @desc     :
 */

@Service
public class CommonService {
	
	private Logger logger = Logger.getLogger(this.getClass());

	public BoardPageInfo getCountPages(Long page, Long numberPosts, Integer numberPages) {
		
		BoardPageInfo boardPageInfo = new BoardPageInfo();

		Long totalPages = numberPosts / CommonConst.BOARD_LINE_NUMBER;
		Long prevPage = (long) 1;
		Long nextPage = (long) 1;
		
		
		if ((totalPages % CommonConst.BOARD_LINE_NUMBER) > 0 || (numberPosts - CommonConst.BOARD_LINE_NUMBER) <= 0) {
			totalPages++; 
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
