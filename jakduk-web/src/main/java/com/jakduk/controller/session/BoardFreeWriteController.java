package com.jakduk.controller.session;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jakduk.model.web.BoardFreeWrite;
import com.jakduk.service.BoardFreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/board")
@SessionAttributes({"boardFreeWrite","boardCategories"})
public class BoardFreeWriteController {

	@Autowired
	private BoardFreeService boardFreeService;


	@RequestMapping(value = "/free/edit", method = RequestMethod.POST)
	public String freeEdit(@Valid BoardFreeWrite boardFreeWrite, BindingResult result, SessionStatus sessionStatus
			, HttpServletRequest request) {

		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "board/freeEdit";
		}
		
		boardFreeService.checkBoardFreeEdit(boardFreeWrite, result);
		
		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("result=" + result);
			}
			return "board/freeEdit";
		}
		
		//Integer status = boardFreeService.edit(request, boardFreeWrite);
		sessionStatus.setComplete();
		
		return "redirect:/board/free/" + boardFreeWrite.getSeq();
	}

}
