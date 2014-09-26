package com.jakduk.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.model.db.Encyclopedia;
import com.jakduk.repository.EncyclopediaRepository;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Service
public class HomeService {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private EncyclopediaRepository encyclopediaRepository;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public void getJumbotron(Model model, String language) {
		
		Integer count = encyclopediaRepository.countByLanguage(language);
		int random = (int)(Math.random() * count) + 1;
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(random, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
	}
}
