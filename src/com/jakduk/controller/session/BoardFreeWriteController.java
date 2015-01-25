package com.jakduk.controller.session;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.jakduk.model.web.BoardFreeWrite;
import com.jakduk.service.BoardFreeService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 8.
 * @desc     :
 */

@Controller
@RequestMapping("/board")
@SessionAttributes({"boardFreeWrite","boardCategorys"})
public class BoardFreeWriteController {

	@Autowired
	private BoardFreeService boardFreeService;

	private Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping(value = "/free/write", method = RequestMethod.GET)
	public String freeWrite(Model model) {

		boardFreeService.getWrite(model);

		return "board/freeWrite";
	}

	@RequestMapping(value = "/free/write", method = RequestMethod.POST)
	public String freeWrite(@Valid BoardFreeWrite boardFreeWrite, BindingResult result, SessionStatus sessionStatus) {
		
		logger.debug("boardFreeWrite=" + boardFreeWrite);

		if (result.hasErrors()) {
			if (logger.isDebugEnabled()) {
				logger.debug("result=" + result);	
			}
			return "board/freeWrite";
		}

		boardFreeService.write(boardFreeWrite);
		sessionStatus.setComplete();

		return "redirect:/board/free";
	}

	@RequestMapping(value = "/free/edit/{seq}", method = RequestMethod.GET)
	public String freeEdit(@PathVariable int seq, Model model,
			HttpServletResponse response) throws IOException {

		Integer status = boardFreeService.getEdit(model, seq, response);
		
		if (!status.equals(HttpServletResponse.SC_OK)) {
			response.sendError(status);
			return null;
		}

		return "board/freeEdit";
	}

	@RequestMapping(value = "/free/edit", method = RequestMethod.POST)
	public String freeEdit(@Valid BoardFreeWrite boardFreeWrite, BindingResult result, SessionStatus sessionStatus) {

		if (result.hasErrors()) {
			if (logger.isDebugEnabled()) {
				logger.debug("result=" + result);	
			}
			return "board/freeEdit";
		}
		
		boardFreeService.checkBoardFreeEdit(boardFreeWrite, result);
		
		if (result.hasErrors()) {
			if (logger.isDebugEnabled()) {
				logger.debug("result=" + result);	
			}
			return "board/freeEdit";
		}
		
		boardFreeService.edit(boardFreeWrite);
		sessionStatus.setComplete();
		
		return "redirect:/board/free/" + boardFreeWrite.getSeq();
	}

}
