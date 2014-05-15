package com.jakduk.service;

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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.authority.AuthUser;
import com.jakduk.common.CommonConst;
import com.jakduk.model.BoardCategory;
import com.jakduk.model.BoardFree;
import com.jakduk.model.BoardSequence;
import com.jakduk.model.BoardWriter;
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
	private MongoTemplate mongoTemplate;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void write(BoardFree boardFree) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		if (principal instanceof AuthUser) {
			String userid = ((AuthUser) principal).getUserid();
			String username = ((AuthUser) principal).getUsername();
			
			BoardWriter writer = new BoardWriter();
			writer.setId(userid);
			writer.setUsername(username);
			boardFree.setWriter(writer);
			boardFree.setSeq(getNextSequence(CommonConst.BOARD_NAME_FREE));
			
			boardFreeRepository.save(boardFree);
			logger.debug("boardFree=" + boardFree);
		} else {
			logger.error("no writer.");
		}		
	}

	public Long getNextSequence(Integer name) {
		
		Long returnVal = Long.valueOf(-1);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Update update = new Update();
		update.inc("seq", 1);
		
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		
		BoardSequence board = mongoTemplate.findAndModify(query, update, options, BoardSequence.class);
		
		if (board == null) {
			logger.debug("err result=" + board);
			return returnVal;
		} else {
			returnVal = board.getSeq();
			return returnVal;
		}
	}
	
	public Model getFree(Model model, Integer page) {
		
		if (page == null) {
			page = 0;
		}
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable request = new PageRequest(page, 5, sort);
		List<BoardFree> posts = boardFreeRepository.findAll(request).getContent();
		
		Map<String, Date> createDate = new HashMap<String, Date>();
		Map<Integer, String> categoryName = new HashMap<Integer, String>();
		
		for (Integer postIdx=0 ; postIdx<posts.size() ; postIdx++) {
			BoardFree tempPost = posts.get(postIdx);
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
		
		return model;
	}
	
	public Model getWrite(Model model) {
		
		List<BoardCategory> categorys = boardCategoryRepository.findByUsingBoard(CommonConst.BOARD_NAME_FREE);
		
		model.addAttribute("boardFree", new BoardFree());
		model.addAttribute("categorys", categorys);
		
		return model;
	}
}
