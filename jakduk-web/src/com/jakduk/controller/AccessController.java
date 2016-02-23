package com.jakduk.controller;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.service.CommonService;
import com.jakduk.service.EmailService;
import com.jakduk.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping()
public class AccessController {

	@Autowired
	private CommonService commonService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

	@Value("#{tokenTerminationTrigger.span}")
	private long tokenSpan;

	private Logger logger = Logger.getLogger(this.getClass());
	
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request,
			Model model,
			@RequestParam(required = false) String result,
			@RequestParam(required = false) String loginRedirect) throws UnsupportedEncodingException {
		
		if (loginRedirect == null) {
			loginRedirect = request.getHeader("REFERER");
		}
		
		if (loginRedirect != null) {
			model.addAttribute("loginRedirect", URLEncoder.encode(loginRedirect, "UTF-8"));
		}
		
		model.addAttribute("result", result);
		
		return "access/login";
	}

	@RequestMapping(value = "/logout/success")
	public String logoutSuccess(HttpServletRequest request) {
		
		String redirctUrl = "/";
		String refererUrl = request.getHeader("REFERER");
		
		if (refererUrl != null) {
			if (commonService.isRedirectUrl(refererUrl)) {
				redirctUrl = refererUrl;
			}
		}
		
		return "redirect:" + redirctUrl;
	}
	
	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/reset_password")
	public String resetPassword(
			Model model,
			@RequestParam(value = "code", required = false) String code
	) {
		if (Objects.nonNull(code)) {
			Token token = tokenRepository.findByCode(code);
			if ((Objects.isNull(token) || !token.getCode().equals(code))) {
				model.addAttribute("title", "user.sign.reset.password");
				model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.INVALID);
			} else {
				model.addAttribute("title", "user.placeholder.password");
				model.addAttribute("user_email", token.getEmail());
				model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.CODE_OK);
			}
			model.addAttribute("action", "confirm_password");
		} else {
			model.addAttribute("action", "reset_password");
			model.addAttribute("title", "user.sign.reset.password");
			model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.NONE);
		}
		return "access/resetPassword";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/reset_password")
	public String sendResetPassword(
			Model model,
			@RequestParam(value = "j_useremail") String email
	) throws UnsupportedEncodingException {
		emailService.sendResetPassword(email);
		model.addAttribute("title", "user.sign.reset.password");
		model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.SEND_OK);
		return "access/resetPassword";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/confirm_password")
	public String updatePassword(
		Model model,
		@RequestParam(value = "j_password") String password,
		@RequestParam(value = "code") String code
	) {
		long tokenSpanMillis = TimeUnit.MINUTES.toMillis(tokenSpan);
		Token token = tokenRepository.findByCode(code);
		if (Objects.isNull(token) || token.getCreatedTime().getTime() + tokenSpanMillis <= System.currentTimeMillis()) {
			model.addAttribute("title", "user.sign.reset.password");
			model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.INVALID);
		} else {
			userService.userPasswordUpdateByEmail(token.getEmail(), password);
			model.addAttribute("title", "user.placeholder.password");
			model.addAttribute("user_email", token.getEmail());
			model.addAttribute("result", CommonConst.RESET_PASSWORD_RESULT.CHANGE_OK);
		}
		if (Objects.nonNull(token)) {
			tokenRepository.delete(token);
		}
		return "access/resetPassword";
	}

}
