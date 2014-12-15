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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.authentication.common.CommonPrincipal;
import com.jakduk.common.CommonConst;
import com.jakduk.model.db.BoardCategory;
import com.jakduk.model.db.BoardFree;
import com.jakduk.model.db.BoardFreeComment;
import com.jakduk.model.embedded.BoardUser;
import com.jakduk.model.embedded.BoardWriter;
import com.jakduk.model.simple.BoardFreeOnComment;
import com.jakduk.model.simple.BoardFreeOnList;
import com.jakduk.model.web.BoardListInfo;
import com.jakduk.model.web.BoardPageInfo;
import com.jakduk.repository.BoardCategoryRepository;
import com.jakduk.repository.BoardFreeCommentRepository;
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
	private BoardFreeCommentRepository boardFreeCommentRepository;
	
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
		String type = principal.getType();

		BoardWriter writer = new BoardWriter();
		writer.setUserId(userid);
		writer.setUsername(username);
		writer.setType(type);
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
		Map<String, String> categoryResName = new HashMap<String, String>();
		List<BoardFreeOnList> posts = new ArrayList<BoardFreeOnList>();
		Long numberPosts = (long) 0;
		
		Integer page = boardListInfo.getPage();
		String categoryName = boardListInfo.getCategory();
		
		if (page < 1) {
			page = 1;
			boardListInfo.setPage(page);
		}
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(page - 1, CommonConst.BOARD_LINE_NUMBER, sort);
		
		if (categoryName != null && 
				(categoryName.equals(CommonConst.BOARD_CATEGORY_NONE) || categoryName.equals(CommonConst.BOARD_CATEGORY_ALL))) {
			posts = boardFreeOnListRepository.findAll(pageable).getContent();
			numberPosts = boardFreeOnListRepository.count();
		} else {
			posts = boardFreeRepository.findByCategoryName(categoryName, pageable).getContent();
			numberPosts = boardFreeRepository.countByCategoryName(categoryName);
		}
		
		BoardPageInfo boardPageInfo = commonService.getCountPages(page.longValue(), numberPosts, 5);
		
//		logger.debug("countAll=" + boardPageInfo);
		
		for (BoardFreeOnList tempPost : posts) {
			String tempId = tempPost.getId();
			String tempCategoryName = tempPost.getCategoryName();
			ObjectId objId = new ObjectId(tempId);
			createDate.put(tempId, objId.getDate());
			
			if (tempCategoryName != null && !categoryResName.containsKey(tempCategoryName)) {
				BoardCategory boardCategory = boardCategoryRepository.findByName(tempCategoryName);
				categoryResName.put(tempCategoryName, boardCategory.getResName());
			}
		}
		
		List<BoardCategory> categorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		model.addAttribute("posts", posts);
		model.addAttribute("createDate", createDate);
		model.addAttribute("categorys", categorys);
		model.addAttribute("usingCategoryResNames", categoryResName);
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
			BoardCategory boardCategory = boardCategoryRepository.findByName(boardFree.getCategoryName());
			
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
			
			if (userid.equals(writer.getUserId())) {
				errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_WRITER;
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (BoardUser boardUser : usersLiking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}
			
			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				for (BoardUser boardUser : usersDisliking) {
					if (boardUser != null && userid.equals(boardUser.getUserId())) {
						errCode = CommonConst.BOARD_USERS_FEELINGS_STATUS_ALREADY;
						break;
					}
				}
			}

			if (errCode.equals(CommonConst.BOARD_USERS_FEELINGS_STATUS_NONE)) {
				BoardUser boardUser = new BoardUser();
				boardUser.setUserId(userid);
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
	
	public void freeCommentWrite(Integer seq, String content) {
		BoardFreeComment boardFreeComment = new BoardFreeComment();
		
		if (!commonService.isAnonymousUser()) {
			BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
			boardFreeComment.setBoardFree(boardFreeOnComment);
			
			CommonPrincipal principal = userService.getCommonPrincipal();
			BoardWriter writer = new BoardWriter();
			writer.setUserId(principal.getId());
			writer.setUsername(principal.getUsername());
			writer.setType(principal.getType());
			boardFreeComment.setWriter(writer);
			
			boardFreeComment.setContent(content);
			
			boardFreeCommentRepository.save(boardFreeComment);
		} else {
			
		}
	}
	
	public void getFreeComment(Model model, int seq, Integer page) {
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
		
		if (page == null) page = 1;
		
		Sort sort = new Sort(Sort.Direction.ASC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(page - 1, 5, sort);
		
		List<BoardFreeComment> comments = boardFreeCommentRepository.findByBoardFree(boardFreeOnComment, pageable).getContent();
		
		model.addAttribute("comments", comments);
	}
	
	public void getFreeCommentCount(Model model, int seq) {
		BoardFreeOnComment boardFreeOnComment = boardFreeRepository.boardFreeOnCommentFindBySeq(seq);
		
		Integer count = boardFreeCommentRepository.countByBoardFree(boardFreeOnComment);
		
		model.addAttribute("count", count);
	}
}
