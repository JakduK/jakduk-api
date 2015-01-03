package com.jakduk.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.db.FootballClub;
import com.jakduk.model.db.FootballClubOrigin;
import com.jakduk.model.embedded.FootballClubName;
import com.jakduk.model.web.BoardCategoryWrite;
import com.jakduk.model.web.FootballClubWrite;
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
			List<String> usingBoard = new ArrayList<String>();
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory01.setName(CommonConst.BOARD_CATEGORY_FREE);
			boardCategory01.setResName("board.category.free");
			boardCategory01.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory01);
			
			usingBoard.clear();
			
			BoardCategory boardCategory02 = new BoardCategory();
			boardCategory02.setResName("board.category.football");
			boardCategory02.setName(CommonConst.BOARD_CATEGORY_FOOTBALL);
			usingBoard.add(CommonConst.BOARD_NAME_FREE);
			boardCategory02.setUsingBoard(usingBoard);
			boardCategoryRepository.save(boardCategory02);
			
			usingBoard.clear();
			
			BoardCategory boardCategory03 = new BoardCategory();
			boardCategory03.setResName("board.category.develop");
			boardCategory03.setName(CommonConst.BOARD_CATEGORY_DEVELOP);
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
	
	public void encyclopediaWrite(Encyclopedia encyclopedia) {
		
		if (encyclopedia.getId() == null) {
			if (encyclopedia.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_EN));			
			} else if (encyclopedia.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				encyclopedia.setSeq(commonService.getNextSequence(CommonConst.ENCYCLOPEDIA_KO));
			}
		}
		
		encyclopediaRepository.save(encyclopedia);
	}
	
	public Model getFootballClubWrite(Model model) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", new FootballClubWrite());
		
		return model;
	}
	
	public Model getFootballClubWrite(Model model, String id) {
		
		List<FootballClubOrigin> footballClubs = footballClubOriginRepository.findAll();
		FootballClub fc = footballClubRepository.findById(id);
		FootballClubWrite fcWrite = new FootballClubWrite();
		fcWrite.setId(fc.getId());
		fcWrite.setActive(fc.getActive());
		fcWrite.setOrigin(fc.getOrigin().getName());
		
		for (FootballClubName fcName : fc.getNames()) {
			if (fcName.getLanguage().equals(Locale.KOREAN.getLanguage())) {
				fcWrite.setFullNameKr(fcName.getFullName());
				fcWrite.setShortNameKr(fcName.getShortName());
			} else if (fcName.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
				fcWrite.setFullNameEn(fcName.getFullName());
				fcWrite.setShortNameEn(fcName.getShortName());
			}
		}
		
		model.addAttribute("footballClubs", footballClubs);
		model.addAttribute("footballClubWrite", fcWrite);
		
		return model;
	}
	
	public void footballClubWrite(FootballClubWrite footballClubWrite) {
		FootballClub footballClub = new FootballClub();
		
		FootballClubOrigin footballClubOrigin = footballClubOriginRepository.findOne(footballClubWrite.getOrigin());
		
		footballClub.setOrigin(footballClubOrigin);
		footballClub.setActive(footballClubWrite.getActive());
		
		if (footballClubWrite.getId() != null) {
			footballClub.setId(footballClubWrite.getId());
		}
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
	
	public void boardCategoryWrite(BoardCategoryWrite boardCategoryWrite) {
		BoardCategory boardCategory = new BoardCategory();
		
		if (boardCategoryWrite.getId() != null) {
			boardCategory.setId(boardCategoryWrite.getId());
		}
		
		boardCategory.setName(boardCategoryWrite.getName());
		boardCategory.setResName(boardCategoryWrite.getResName());
		
		String[] originUsingBoard = boardCategoryWrite.getUsingBoard();

		if (originUsingBoard != null) {
			ArrayList<String> usingBoard = new ArrayList<String>(Arrays.asList(originUsingBoard));
			boardCategory.setUsingBoard(usingBoard);			
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("boardCategory=" + boardCategory);
		}
		
		boardCategoryRepository.save(boardCategory);
	}
	
	public Model getEncyclopediaList(Model model) {
		List<Encyclopedia> encyclopedias = encyclopediaRepository.findAll();
		
		model.addAttribute("encyclopedias", encyclopedias);
		
		return model;
	}
	
	public Model getEncyclopedia(Model model, int seq, String language) {
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(seq, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
		
		return model;
	}
	
	public Model getFootballClubOriginList(Model model) {
		List<FootballClubOrigin> fcOrigins = footballClubOriginRepository.findAll();
		
		model.addAttribute("fcOrigins", fcOrigins);
		
		return model;
	}
	
	public Model getFootballClubOrigin(Model model, String id) {
		FootballClubOrigin fcOrigin = footballClubOriginRepository.findOne(id);
		
		model.addAttribute("footballClubOrigin", fcOrigin);
		
		return model;
	}
	
	public Model getFootballClubList(Model model) {
		List<FootballClub> fcs = footballClubRepository.findAll();
		
		model.addAttribute("fcs", fcs);
		
		return model;
	}
	
	public Model getBoardCategoryList(Model model) {
		List<BoardCategory> boardCategorys = boardCategoryRepository.findAll();
		
		model.addAttribute("boardCategorys", boardCategorys);
		
		return model;
	}
	
	public Model getBoardCategory(Model model, String id) {
		BoardCategory boardCategory = boardCategoryRepository.findOne(id);
		
		BoardCategoryWrite boardCategoryWrite = new BoardCategoryWrite();
		boardCategoryWrite.setId(boardCategory.getId());
		boardCategoryWrite.setName(boardCategory.getName());
		boardCategoryWrite.setResName(boardCategory.getResName());
		String[] usingBoard = boardCategory.getUsingBoard().toArray(new String[boardCategory.getUsingBoard().size()]);
		boardCategoryWrite.setUsingBoard(usingBoard);
		
		model.addAttribute("boardCategoryWrite", boardCategoryWrite);
		
		return model;
	}
	
}
