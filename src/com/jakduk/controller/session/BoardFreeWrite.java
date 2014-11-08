package com.jakduk.controller.session;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jakduk.model.db.BoardFree;
import com.jakduk.service.BoardFreeService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Controller
@RequestMapping("/board")
@SessionAttributes({"boardFree","boardCategorys"})
public class BoardFreeWrite {
	
	@Autowired
	private BoardFreeService boardFreeService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping(value = "/free/write", method = RequestMethod.GET)
	public String freeWrite(Model model) {
		
		boardFreeService.getWrite(model);
		
		return "board/freeWrite";
	}
	
	@RequestMapping(value = "/free/write", method = RequestMethod.POST)
	public String freeWrite(@Valid BoardFree boardFree, BindingResult result, SessionStatus sessionStatus) {
		
		if (result.hasErrors()) {
			return "board/freeWrite";
		}
		
		boardFreeService.write(boardFree);
		sessionStatus.setComplete();
		
		return "redirect:/board/free";
	}
	
}
