package com.jakduk.restcontroller.user;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import com.jakduk.common.CommonConst;
import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.service.CommonService;
import com.jakduk.notification.EmailService;
import com.jakduk.service.UserService;

@Slf4j
@Api(tags = "회원 비밀번호", description = "회원 비밀번호 API")
@RestController
@RequestMapping("/api/password")
public class PasswordRestController {

	@Resource
	LocaleResolver localeResolver;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Value("#{tokenTerminationTrigger.span}")
	private long tokenSpan;

	// jakduk 비밀번호 찾기 처리.
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public Map<String, Object> findPassword(@RequestParam String email,
	                                        @RequestParam String callbackUrl,
	                                        HttpServletRequest request) {

		Locale locale = localeResolver.resolveLocale(request);

		String message = "";

		com.jakduk.model.simple.UserProfile userProfile = userService.findOneByEmail(email);

		if (Objects.isNull(userProfile)) {
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.are.not.registered");
		} else {
			switch (userProfile.getProviderId()) {
				case JAKDUK:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.sendok");
					emailService.sendResetPassword(callbackUrl, email);
					break;
				case DAUM:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns", CommonConst.ACCOUNT_TYPE.DAUM);
					break;
				case FACEBOOK:
					message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.you.connect.with.sns", CommonConst.ACCOUNT_TYPE.FACEBOOK);
					break;
			}
		}

		Map<String, Object> response = new HashMap<>();

		response.put("subject", email);
		response.put("message", message);

		return response;
	}

	// jakduk 비밀번호 재설정.
	@RequestMapping(value = "/reset/{code}", method = RequestMethod.GET)
	public Map<String, Object> resetPassword(@PathVariable String code,
	                            HttpServletRequest request) {

		Locale locale = localeResolver.resolveLocale(request);

		if (Objects.isNull(code))
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.common", "common.exception.invalid.parameter"));

		Map<String, Object> response = new HashMap<>();
		Token token = commonService.getTokenByCode(code);

		if (Objects.isNull(token) || !token.getCode().equals(code)) {
			throw new IllegalArgumentException(commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.invalid"));
		}

		response.put("subject", token.getEmail());
		response.put("code", token.getCode());

		return response;
	}

	// jakduk 비밀번호 재설정 처리.
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public Map<String, Object> updatePassword(@RequestParam(value = "password") String password,
	                             @RequestParam(value = "code") String code,
	                             HttpServletRequest request) {

		Locale locale = localeResolver.resolveLocale(request);

		long tokenSpanMillis = TimeUnit.MINUTES.toMillis(tokenSpan);
		Token token = tokenRepository.findByCode(code);

		Map<String, Object> response = new HashMap<>();
		String message;

		if (Objects.isNull(token) || token.getCreatedTime().getTime() + tokenSpanMillis <= System.currentTimeMillis()) {
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.reset.password.invalid");
		} else {
			userService.userPasswordUpdateByEmail(token.getEmail(), password);
			message = commonService.getResourceBundleMessage(locale, "messages.user", "user.msg.success.change.password");
			response.put("subject", token.getEmail());
		}

		if (Objects.nonNull(token)) {
			tokenRepository.delete(token);
		}

		response.put("message", message);

		return response;
	}
}
