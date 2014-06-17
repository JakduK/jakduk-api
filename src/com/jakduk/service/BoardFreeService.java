package com.jakduk.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.authority.AuthUser;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardUser;
import com.jakduk.model.db.BoardWriter;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.model.web.BoardPageInfo;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.BoardSequenceRepository;
import com.jakduk.repository.UserRepository;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardSequenceRepository boardSequenceRepository;
	
	@Autowired
	private BoardCategoryRepository boardCategoryRepository;
	
	@Autowired
	private CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 자유게시판 글쓰기 페이지
	 * @param model
	 * @return
	 */
	public Model getWrite(Model model) {
		
		List<BoardCategory> boardCategorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		model.addAttribute("boardFree", new BoardFree());
		model.addAttribute("boardCategorys", boardCategorys);
		
		return model;
	}
	
	/**
	 * 자유게시판 글쓰기 데이터 DB에 삽입
	 * @param boardFree
	 */
	public void write(BoardFree boardFree) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof AuthUser) {
			String userid = ((AuthUser) principal).getUserid();
			String username = ((AuthUser) principal).getUsername();
			
			BoardWriter writer = new BoardWriter();
			writer.setId(userid);
			writer.setUsername(username);
			boardFree.setWriter(writer);
			boardFree.setSeq(commonService.getNextSequence(CommonConst.BOARD_NAME_FREE));
			
			boardFreeRepository.save(boardFree);
			logger.debug("boardFree=" + boardFree);
		} else {
			logger.error("no writer.");
		}		
	}
	
	/**
	 * 자유게시판 목록 페이지
	 * @param model
	 * @param boardListInfo
	 * @return
	 */
	public Model getFree(Model model, BoardListInfo boardListInfo) {

		Map<String, Date> createDate = new HashMap<String, Date>();
		Map<Integer, String> categoryName = new HashMap<Integer, String>();
		List<BoardFree> posts = new ArrayList<BoardFree>();
		Long numberPosts = (long) 0;
		
		Integer page = boardListInfo.getPage();
		Integer categoryId = boardListInfo.getCategory();
		
		if (page < 1) {
			page = 1;
			boardListInfo.setPage(page);
		}
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(page - 1, CommonConst.BOARD_LINE_NUMBER, sort);
		
		if (categoryId == CommonConst.BOARD_CATEGORY_NONE || categoryId == CommonConst.BOARD_CATEGORY_ALL) {
			posts = boardFreeRepository.findAll(pageable).getContent();
			numberPosts = boardFreeRepository.count();
		} else {
			posts = boardFreeRepository.findByCategoryId(categoryId, pageable).getContent();
			numberPosts = boardFreeRepository.countByCategoryId(categoryId);
		}
		
		BoardPageInfo boardPageInfo = commonService.getCountPages(page.longValue(), numberPosts, 5);
		
//		logger.debug("countAll=" + boardPageInfo);
		
		for (BoardFree tempPost : posts) {
			String tempId = tempPost.getId();
			Integer tempCategoryId = tempPost.getCategoryId();
			ObjectId objId = new ObjectId(tempId);
			createDate.put(tempId, objId.getDate());
			
			if (tempCategoryId != null && !categoryName.containsKey(tempCategoryId)) {
				BoardCategory boardCategory = boardCategoryRepository.findByCategoryId(tempCategoryId);
				categoryName.put(tempCategoryId, boardCategory.getName());
			}
		}
		
		List<BoardCategory> categorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		logger.debug("posts=" + posts);
		
		model.addAttribute("posts", posts);
		model.addAttribute("createDate", createDate);
		model.addAttribute("categorys", categorys);
		model.addAttribute("usingCategoryNames", categoryName);
		model.addAttribute("pageInfo", boardPageInfo);
		model.addAttribute("listInfo", boardListInfo);
		
		return model;
	}
	
	/**
	 * 자유게시판 게시물 보기 페이지
	 * @param model
	 * @param seq
	 * @param boardListInfo
	 * @return
	 */
	public Model getFreeView(Model model, int seq, BoardListInfo boardListInfo, Boolean isAddCookie) {
		
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		BoardCategory boardCategory = boardCategoryRepository.findByCategoryId(boardFree.getCategoryId());
		
		ObjectId objId = new ObjectId(boardFree.getId());
		Date createDate = objId.getDate();
		
		if (isAddCookie == true) {
			int views = boardFree.getViews();
			boardFree.setViews(++views);
			logger.debug("post=" + boardFree);
			boardFreeRepository.save(boardFree);
		}
		
		model.addAttribute("post", boardFree);
		model.addAttribute("category", boardCategory);
		model.addAttribute("createDate", createDate);
		model.addAttribute("listInfo", boardListInfo);
		
		return model;
	}
	
	/**
	 * 게시물 좋아요 싫어요
	 * @param model
	 * @param seq
	 * @param status
	 * @return
	 */
	public Model getGoodOrBad(Model model, int seq, int status) {
		Integer errCode = -1;
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		
		List<BoardUser> goodUsers = boardFree.getGoodUsers();
		List<BoardUser> badUsers = boardFree.getBadUsers();
		
		if (goodUsers == null) {
			goodUsers = new ArrayList<BoardUser>(); 
		}
		
		if (badUsers == null) {
			badUsers = new ArrayList<BoardUser>(); 
		}
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal instanceof AuthUser) {
			String userid = ((AuthUser) principal).getUserid();
			String username = ((AuthUser) principal).getUsername();
			
			Boolean isSameUser = false;
			
			for (BoardUser boardUser : goodUsers) {
				if (boardUser != null && userid.equals(boardUser.getUserid())) {
					isSameUser = true;
					errCode = 3; // 이미 좋아요 누름 
					break;
				}
			}
			
			if (isSameUser == false) {
				for (BoardUser boardUser : badUsers) {
					if (boardUser != null && userid.equals(boardUser.getUserid())) {
						isSameUser = true;
						errCode = 3; // 이미 싫어요 누름
						break;
					}
				}
			}
			
			if (isSameUser == false) {
				BoardUser boardUser = new BoardUser();
				boardUser.setUserid(userid);
				boardUser.setUsername(username);
				boardUser.setId(new ObjectId().toString());
				
				if (status == 1) {
					goodUsers.add(boardUser);
					boardFree.setGoodUsers(goodUsers);
					boardFreeRepository.save(boardFree);
					errCode = 1; // 좋아요
				} else if (status == 2) {
					badUsers.add(boardUser);
					boardFree.setBadUsers(badUsers);
					boardFreeRepository.save(boardFree);
					errCode = 2; // 싫어요
				} 
			}
		} else if (principal.equals("anonymousUser")) {
			errCode = 4; // 로그인 안했음
		}	
		
		model.addAttribute("errorCode", errCode);
		model.addAttribute("numberOfGood", goodUsers.size());
		model.addAttribute("numberOfBad", badUsers.size());
		return model;
	}
}
