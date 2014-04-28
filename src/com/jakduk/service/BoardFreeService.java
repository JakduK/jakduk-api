package com.jakduk.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.authority.AuthUser;
import com.jakduk.model.BoardFree;
import com.jakduk.model.BoardSequence;
import com.jakduk.model.BoardWriter;
import com.jakduk.model.User;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.BoardSequenceRepository;
import com.jakduk.repository.UserRepository;
import com.mongodb.BasicDBObject;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardSequenceRepository boardSequenceRepository;
	
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
			boardFree.setSeq(getNextSequence("free"));
			
			boardFreeRepository.save(boardFree);
			logger.debug("boardFree=" + boardFree);
		} else {
			logger.error("no writer.");
		}		
	}

	public List<BoardFree> findAll() {
		User writer = userRepository.writerFindById("5359d92ce4b0efcb45ea8ba2");
		logger.debug("writer=" + writer);
		
		return boardFreeRepository.findAll();
	}	
	
	public Long getNextSequence(String name) {
		
		Long returnVal = Long.valueOf(-1);
		
		BoardSequence getBoardSequence = boardSequenceRepository.findByName(name);
		
		if (getBoardSequence == null) {
			BoardSequence boardSequence = new BoardSequence();
			boardSequence.setName(name);
			boardSequenceRepository.save(boardSequence);
		}
		
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
	
	public Model getFree(Model model) {
		
		List<BoardFree> posts = boardFreeRepository.findAll();
		
		Map<String, Object> extraInfo = new HashMap<String, Object>();
		Map<String, Date> createDate = new HashMap<String, Date>();
		
		for (Integer postIdx=0 ; postIdx<posts.size() ; postIdx++) {
			BoardFree tempPost = posts.get(postIdx);
			String tempId = tempPost.getId();
			ObjectId objId = new ObjectId(tempId);
			createDate.put(tempId, objId.getDate());
		}
		
		
		
//		extraInfo.put("createDate", createDate);
		
		model.addAttribute("posts", posts);
		model.addAttribute("createDate", createDate);
		
		return model;
	}
}
