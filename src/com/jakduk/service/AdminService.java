package com.jakduk.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.authentication.common.CommonUserDetails;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.embedded.FootballClubName;
import com.jakduk.model.web.FootballClubWrite;
import com.jakduk.model.web.OAuthUserWrite;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.EncyclopediaRepository;
import com.jakduk.repository.FootballClubOriginRepository;
import com.jakduk.repository.FootballClubRepository;
import com.jakduk.repository.SequenceRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 5. 1.
 * @desc     :
 */

@Service
public class AdminService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private BoardCategoryRepository boardCategoryRepository;
	
	@Autowired
	private SequenceRepository sequenceRepository;
	
	@Autowired
	private EncyclopediaRepository encyclopediaRepository;
	
	@Autowired
	private FootballClubRepository footballClubRepository;
	
	@Autowired
	private FootballClubOriginRepository footballClubOriginRepository;

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
			
			BoardCategory boardCategory03 = new BoardCategory();
			boardCategory03.setName("board.category.develop");
			boardCategory03.setCategoryId(CommonConst.BOARD_CATEGORY_DEVELOP);
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory03.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory03);
			
			logger.debug("input board category.");
			result = "success input board category data at DB";
		} else {
			result = "already exist board category at DB.";
		}
		
		return result;
	}
	
	public void encyclopediaWrite(Encyclopedia shortHistory) {
		
		if (shortHistory.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			shortHistory.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_EN));			
		} else if (shortHistory.getLanguage().equals(Locale.KOREAN.getLanguage())) {
			shortHistory.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_KO));
		}
		
		encyclopediaRepository.save(shortHistory);
	}
	
	public Model getFootballClubWrite(Model model) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", new FootballClubWrite());
		
		return model;
	}
	
	public void footballClubWrite(FootballClubWrite footballClubWrite) {
		FootballClub footballClub = new FootballClub();
		
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(footballClubWrite.getOrigin());
		
		footballClub.setOrigin(footballClubOrigin);
		footballClub.setActive(footballClubWrite.getActive());
		
		ArrayList<FootballClubName> names = new ArrayList<FootballClubName>();
		FootballClubName footballClubNameKr = new FootballClubName();
		FootballClubName footballClubNameEn = new FootballClubName();
		footballClubNameKr.setLanguage(Locale.KOREAN.getLanguage());
		footballClubNameKr.setShortName(footballClubWrite.getShortNameKr());
		footballClubNameKr.setFullName(footballClubWrite.getFullNameKr());
		footballClubNameEn.setLanguage(Locale.ENGLISH.getLanguage());
		footballClubNameEn.setShortName(footballClubWrite.getShortNameEn());
		footballClubNameEn.setFullName(footballClubWrite.getFullNameEn());
		names.add(footballClubNameKr);
		names.add(footballClubNameEn);
		footballClub.setNames(names);
		
		footballClubRepository.save(footballClub);
	}
	
	public void footballClubOriginWrite(FootballClubOrigin footballClubOrigin) {
		footballClubOriginRepository.save(footballClubOrigin);
	}
}
