package com.jakduk.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jakduk.model.BoardFree;
import com.jakduk.repository.BoardFreeRepository;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void write(BoardFree boardFree) {
		boardFreeRepository.save(boardFree);
	}

}
