package com.jakduk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.dao.JakdukDAO;
import com.jakduk.dao.SupporterCount;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

@Service
public class StatsService {
	
	@Autowired
	private JakdukDAO jakdukDAO;

	public void getSupporter(Model model, String language) {
		
		List<SupporterCount> supporters = jakdukDAO.getSupportFCCount(language);
		
		model.addAttribute("supporters", supporters);
		
	}
}
