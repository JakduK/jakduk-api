package com.jakduk.api.controller;

import com.jakduk.core.common.CommonConst;
import com.jakduk.core.model.db.Token;
import com.jakduk.core.model.simple.UserProfile;
import com.jakduk.core.notification.EmailService;
import com.jakduk.core.repository.TokenRepository;
import com.jakduk.core.service.CommonService;
import com.jakduk.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/")
public class AccessController {

	@Autowired
	private CommonService commonService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Resource
	LocaleResolver localeResolver;

	@Value("#{tokenTerminationTrigger.span}")
	private long tokenSpan;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(required = false) String result,
						@RequestParam(required = false) String message,
						@RequestParam(required = false) String loginRedirect,
						HttpServletRequest request,
						Model model) throws UnsupportedEncodingException {
		
		if (loginRedirect == null) {
			loginRedirect = request.getHeader("REFERER");
		}
		
		if (loginRedirect != null) {
			model.addAttribute("loginRedirect", URLEncoder.encode(loginRedirect, "UTF-8"));
		}

		if (Objects.nonNull(message) && message.isEmpty() == false)
			model.addAttribute("message", message);
		
		model.addAttribute("result", result);
		
		return "access/login";
	}

	// SNS로 회원 가입할때.
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String redirectRequestToRegistrationPage() {

		return "redirect:/user/social";
	}

	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}

	// jakduk 비밀번호 찾기 페이지.
	@RequestMapping(value = "/password/find", method = RequestMethod.GET)
	public String findPassword() {
		return "access/passwordFind";
	}

	// jakduk 비밀번호 찾기 처리.
	@RequestMapping(value = "/password/find", method = RequestMethod.POST)
	public String findPassword(@RequestParam String email,
							   HttpServletRequest request,
							   Model model) {

		Locale locale = localeResolver.resolveLocale(request);

		String message = "";

		String host = UrlUtils.buildFullRequestUrl(request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath(), null);
		UserProfile userProfile = userService.findOneByEmail(email);

		if (Objects.isNull(userProfile)) {
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.are.not.registered");
		} else {
			switch (userProfile.getProviderId()) {
				case JAKDUK:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.sendok");
					emailService.sendResetPassword(host, email);
					break;
				case DAUM:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns", CommonConst.ACCOUNT_TYPE.DAUM);
					break;
				case FACEBOOK:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns", CommonConst.ACCOUNT_TYPE.FACEBOOK);
					break;
			}
		}

		model.addAttribute("subject", email);
		model.addAttribute("message", message);

		return "access/passwordFindMessage";
	}

	// jakduk 비밀번호 재설정 페이지.
	@RequestMapping(value = "/password/reset/{code}", method = RequestMethod.GET)
	public String resetPassword(@PathVariable String code,
								HttpServletRequest request,
								Model model) {

		Locale locale = localeResolver.resolveLocale(request);

		if (Objects.isNull(code))
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));

		Token token = commonService.getTokenByCode(code);

		if (Objects.isNull(token) || token.getCode().equals(code) == false) {
			model.addAttribute("message", commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.invalid"));
			return "access/passwordFindMessage";
		}

		model.addAttribute("subject", token.getEmail());
		model.addAttribute("code", token.getCode());

		return "access/passwordReset";
	}

	// jakduk 비밀번호 재설정 처리.
	@RequestMapping(value = "/password/reset", method = RequestMethod.POST)
	public String updatePassword(@RequestParam(value = "password") String password,
								 @RequestParam(value = "code") String code,
								 HttpServletRequest request,
								 Model model) {

		Locale locale = localeResolver.resolveLocale(request);

		long tokenSpanMillis = TimeUnit.MINUTES.toMillis(tokenSpan);
		Token token = tokenRepository.findByCode(code);

		String message = "";

		if (Objects.isNull(token) || token.getCreatedTime().getTime() + tokenSpanMillis <= System.currentTimeMillis()) {
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.invalid");
		} else {
			userService.userPasswordUpdateByEmail(token.getEmail(), password);
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.success.change.password");
			model.addAttribute("subject", token.getEmail());
		}

		if (Objects.nonNull(token)) {
			tokenRepository.delete(token);
		}

		model.addAttribute("message", message);

		return "access/passwordFindMessage";
	}

}
