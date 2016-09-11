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

	@RequestMapping(value = "/denied")
	public String denied() {
		return "access/denied";
	}

}
