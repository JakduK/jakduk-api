package org.jakduk.service;

import org.apache.log4j.Logger;
import org.jakduk.model.BoardFree;
import org.jakduk.repository.BoardFreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardFreeService {
	
	@Autowired
	private BoardFreeRepository boardFreeRepository;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void write(BoardFree boardFree) {
		boardFreeRepository.save(boardFree);
	}

}
