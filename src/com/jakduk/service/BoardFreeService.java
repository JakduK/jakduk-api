package com.jakduk.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jakduk.authority.AuthUser;
import com.jakduk.model.BoardFree;
import com.jakduk.model.User;
import com.jakduk.repository.BoardFreeRepository;
import com.jakduk.repository.UserRepository;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void write(BoardFree boardFree) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
		if (principal instanceof AuthUser) {
			String userid = ((AuthUser) principal).getUserid();
			User writer = userRepository.writerFindById(userid);
			boardFree.setWriter(writer);
			
			logger.debug("boardFree=" + boardFree);
			
			boardFreeRepository.save(boardFree);
		} else {
			logger.error("no writer.");
		}		
	}

	public List<BoardFree> findAll() {
		User writer = userRepository.writerFindById("5359d92ce4b0efcb45ea8ba2");
		logger.debug("writer=" + writer);
		
		return boardFreeRepository.findAll();
	}
}
