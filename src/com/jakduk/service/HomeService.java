package com.jakduk.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
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
	private EncyclopediaRepository encyclopediaRepository;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public void getJumbotron(Model model, String lang) {
		
		Integer language = -1;
		
		if (lang == null) {
			language = CommonConst.LANGUAGE_EN;
		} else if (lang.contains("en")) {
			language = CommonConst.LANGUAGE_EN;
		} else if (lang.contains("ko")) {
			language = CommonConst.LANGUAGE_KO;
		} else {
			language = CommonConst.LANGUAGE_EN;
		}
		
		Integer count = encyclopediaRepository.countByLanguage(language);
		int random = (int)(Math.random() * count) + 1;
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(random, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
	}
}
