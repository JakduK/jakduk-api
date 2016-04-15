package com.jakduk.controller.session;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.model.web.UserWrite;
import com.jakduk.service.CommonService;
import com.jakduk.service.UserService;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 27.
 * @desc     :
 */

@Controller
@Slf4j
@RequestMapping("/user")
@SessionAttributes({"userWrite", "footballClubs"})
public class UserWriteController {
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Resource
	LocaleResolver localeResolver;

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) String lang,
			Model model) {
		
		Locale locale = localeResolver.resolveLocale(request);
		String language = commonService.getLanguageCode(locale, lang);
		
		userService.getUserWrite(model, language);
		
		return "user/write";
	}
	
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@Valid UserWrite userWrite, BindingResult result, SessionStatus sessionStatus,
			HttpServletRequest request, HttpServletResponse response) {

		Locale locale = localeResolver.resolveLocale(request);

		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/write";
		}

		String pwd = userWrite.getPassword();
		String pwdCfm = userWrite.getPasswordConfirm();

		try {
			userService.existEmail(locale, userWrite.getEmail());
		} catch (RuntimeException e) {
			result.rejectValue("email", "user.msg.already.email");
		}

		try {
			userService.existUsernameOnWrite(locale, userWrite.getUsername());
		} catch (RuntimeException e) {
			result.rejectValue("username", "user.msg.already.username");
		}

		if (!pwd.equals(pwdCfm)) {
			result.rejectValue("passwordConfirm", "user.msg.password.mismatch");
		}

		if (result.hasErrors()) {
			log.debug("result=" + result);
			return "user/write";
		}
		
		userService.userWrite(userWrite);
		sessionStatus.setComplete();
		
		String path = String.format("%s/", request.getContextPath());
		
		commonService.setCookie(response, CommonConst.COOKIE_EMAIL, userWrite.getEmail(), path);
		
		commonService.setCookie(response, CommonConst.COOKIE_REMEMBER, "1", path);
		
		return "redirect:/login?result=success";
	}

}
