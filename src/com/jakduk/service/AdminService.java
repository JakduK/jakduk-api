package com.jakduk.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jakduk.common.CommonConst;
import com.jakduk.model.BoardCategory;
import com.jakduk.model.BoardSequence;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.BoardSequenceRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Service
public class AdminService {
	
	@Autowired
	private BoardCategoryRepository boardCategoryRepository;
	
	@Autowired
	private BoardSequenceRepository boardSequenceRepository;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public String initData() {
		
		String result = "";
		
		if (boardCategoryRepository.count() == 0) {
			BoardCategory boardCategory01 = new BoardCategory();
			List<Integer> usingBoard = new ArrayList<Integer>();
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory01.setName("board.category.free");
			boardCategory01.setCategoryId(CommonConst.BOARD_CATEGORY_FREE);
			boardCategory01.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory01);
			
			BoardCategory boardCategory02 = new BoardCategory();
			boardCategory02.setName("board.category.football");
			boardCategory02.setCategoryId(CommonConst.BOARD_CATEGORY_FOOTBALL);
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory02.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory02);
			
			logger.debug("input board category.");
			result = "success input board category data at DB";
		} else {
			result = "already exist board category at DB.";
		}
		
		BoardSequence getBoardSequence = boardSequenceRepository.findByName(CommonConst.BOARD_NAME_FREE);
		
		if (getBoardSequence == null) {
			BoardSequence boardSequence = new BoardSequence();
			boardSequence.setName(CommonConst.BOARD_NAME_FREE);
			boardSequenceRepository.save(boardSequence);
		}
		
		return result;
	}
}
