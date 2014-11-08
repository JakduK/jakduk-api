package com.jakduk.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.authentication.jakduk.JakdukPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.embedded.BoardUser;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.simple.BoardWriter;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.model.web.BoardPageInfo;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeOnListRepository;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.SequenceRepository;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private BoardFreeOnListRepository boardFreeOnListRepository;
	
	@Autowired
	private SequenceRepository boardSequenceRepository;
	
	@Autowired
	private BoardCategoryRepository boardCategoryRepository;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
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
		
		CommonPrincipal principal = userService.getCommonPrincipal();
		String userid = principal.getId();
		String username = principal.getUsername();
			
			BoardWriter writer = new BoardWriter();
			writer.setId(userid);
			writer.setUsername(username);
			boardFree.setWriter(writer);
			boardFree.setSeq(commonService.getNextSequence(CommonConst.BOARD_NAME_FREE));
			
			boardFreeRepository.save(boardFree);
			
			if (logger.isInfoEnabled()) {
				logger.info("new post created. user id=" + userid + ", username=" + username);
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("boardFree=" + boardFree);
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
		List<BoardFreeOnList> posts = new ArrayList<BoardFreeOnList>();
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
			posts = boardFreeOnListRepository.findAll(pageable).getContent();
			numberPosts = boardFreeOnListRepository.count();
		} else {
			posts = boardFreeOnListRepository.findByCategoryId(categoryId, pageable).getContent();
			numberPosts = boardFreeOnListRepository.countByCategoryId(categoryId);
		}
		
		BoardPageInfo boardPageInfo = commonService.getCountPages(page.longValue(), numberPosts, 5);
		
//		logger.debug("countAll=" + boardPageInfo);
		
		for (BoardFreeOnList tempPost : posts) {
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
		
		try {
			BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
			BoardCategory boardCategory = boardCategoryRepository.findByCategoryId(boardFree.getCategoryId());
			
			ObjectId objId = new ObjectId(boardFree.getId());
			Date createDate = objId.getDate();
			
			if (isAddCookie == true) {
				int views = boardFree.getViews();
				boardFree.setViews(++views);
				boardFreeRepository.save(boardFree);
			}
			
			model.addAttribute("post", boardFree);
			model.addAttribute("category", boardCategory);
			model.addAttribute("createDate", createDate);
			model.addAttribute("listInfo", boardListInfo);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return model;
	}
	
	/**
	 * 게시물 좋아요 싫어요
	 * @param model
	 * @param seq
	 * @param status
	 * @return
	 */
	public Model setUsersFeelings(Model model, int seq, String feeling) {
		
		String errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE;

		CommonPrincipal principal = userService.getCommonPrincipal();
		String userid = principal.getId();
		String username = principal.getUsername();
		
		BoardFree boardFree = boardFreeRepository.findOneBySeq(seq);
		BoardWriter writer = boardFree.getWriter();

		List<BoardUser> usersLiking = boardFree.getUsersLiking();
		List<BoardUser> usersDisliking = boardFree.getUsersDisliking();

		if (usersLiking == null) {
			usersLiking = new ArrayList<BoardUser>(); 
		}
		
		if (usersDisliking == null) {
			usersDisliking = new ArrayList<BoardUser>(); 
		}
		
		if (userid != null && username != null) {
			
			if (userid.equals(writer.getId())) {
				errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (BoardUser boardUser : usersLiking) {
					if (boardUser != null && userid.equals(boardUser.getUserid())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}
			
			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (BoardUser boardUser : usersDisliking) {
					if (boardUser != null && userid.equals(boardUser.getUserid())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				BoardUser boardUser = new BoardUser();
				boardUser.setUserid(userid);
				boardUser.setUsername(username);
				boardUser.setId(new ObjectId().toString());

				switch (feeling) {
				case CommonConst.BOARD_USERS_FEELINGS_TYPE_LIKE:
					usersLiking.add(boardUser);
					boardFree.setUsersLiking(usersLiking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_LIKE; 
					break;
				case CommonConst.BOARD_USERS_FEELINGS_TYPE_DISLIKE:
					usersDisliking.add(boardUser);
					boardFree.setUsersDisliking(usersDisliking);
					errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_DISLIKE;
					break;
				default:
					break;
				}
				
				boardFreeRepository.save(boardFree);
			}
		} else {
			errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ANONYMOUS;
		}
		
		model.addAttribute("errorCode", errCode);
		model.addAttribute("numberOfLike", usersLiking.size());
		model.addAttribute("numberOfDislike", usersDisliking.size());
		
		return model;
	}
}
