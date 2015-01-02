package com.jakduk.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Encyclopedia;
import com.jakduk.model.simple.BoardFreeOnHome;
import com.jakduk.model.simple.UserOnHome;
import com.jakduk.repository.BoardFreeOnHomeRepository;
import com.jakduk.repository.EncyclopediaRepository;
import com.jakduk.repository.UserOnHomeRepository;

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
	
	@Autowired
	private BoardFreeOnHomeRepository boardFreeOnHomeRepository;
	
	@Autowired
	private UserOnHomeRepository userOnHomeRepository;

	private Logger logger = Logger.getLogger(this.getClass());
	
	public Model getHome(Model model, Locale locale) {
		
		try {
			LocalDate date = LocalDate.now();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			DateTimeFormatter format = DateTimeFormatter.ISO_DATE;
			
			Long timeNow = sdf.parse(date.format(format)).getTime();
			
			model.addAttribute("timeNow", timeNow);
			model.addAttribute("dateTimeFormat", commonService.getDateTimeFormat(locale));
		}  catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} 
		
		return model;
	}
	
	public void getJumbotron(Model model, String language) {
		
		Integer count = encyclopediaRepository.countByLanguage(language);
		int random = (int)(Math.random() * count) + 1;
		Encyclopedia encyclopedia = encyclopediaRepository.findOneBySeqAndLanguage(random, language);
		
		model.addAttribute("encyclopedia", encyclopedia);
	}
	
	public Model getBoardLatest(Model model) {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("seq"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<BoardFreeOnHome> posts = boardFreeOnHomeRepository.findAll(pageable).getContent();
		
		model.addAttribute("posts", posts);
		
		return model;
	}
	
	public Model getUserLatest(Model model) {
		
		Sort sort = new Sort(Sort.Direction.DESC, Arrays.asList("_id"));
		Pageable pageable = new PageRequest(0, CommonConst.HOME_SIZE_LINE_NUMBER, sort);
		
		List<UserOnHome> posts = userOnHomeRepository.findAll(pageable).getContent();
		
		model.addAttribute("users", posts);
		
		return model;
	}
}
