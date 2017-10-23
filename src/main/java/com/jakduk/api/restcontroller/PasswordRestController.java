package com.jakduk.api.restcontroller;

import com.jakduk.api.common.Constants;
import com.jakduk.api.common.rabbitmq.RabbitMQPublisher;
import com.jakduk.api.common.util.JakdukUtils;
import com.jakduk.api.model.db.Token;
import com.jakduk.api.model.simple.UserProfile;
import com.jakduk.api.repository.TokenRepository;
import com.jakduk.api.service.CommonService;
import com.jakduk.api.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Api(tags = "User", description = "회원 API")
@RestController
@RequestMapping("/api/password")
public class PasswordRestController {

	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private TokenRepository tokenRepository;
	@Autowired private CommonService commonService;
	@Autowired private UserService userService;
	@Autowired private RabbitMQPublisher rabbitMQPublisher;

	// jakduk 비밀번호 찾기 처리.
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public Map<String, Object> findPassword(@RequestParam String email,
	                                        @RequestParam(value = "callbackUrl") String host,
											Locale locale) {
		String message = "";

		UserProfile userProfile = userService.findOneByEmail(email);

		if (Objects.isNull(userProfile)) {
			message = JakdukUtils.getMessageSource( "user.msg.you.are.not.registered");
		} else {
			switch (userProfile.getProviderId()) {
				case JAKDUK:
					message = JakdukUtils.getMessageSource("user.msg.reset.password.sendok");
					rabbitMQPublisher.sendResetPassword(locale, host, email);
					break;
				case DAUM:
					message = JakdukUtils.getMessageSource("user.msg.you.connect.with.sns", Constants.ACCOUNT_TYPE.DAUM);
					break;
				case FACEBOOK:
					message = JakdukUtils.getMessageSource("user.msg.you.connect.with.sns", Constants.ACCOUNT_TYPE.FACEBOOK);
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
	public Map<String, Object> resetPassword(@PathVariable String code) {

		if (Objects.isNull(code))
			throw new IllegalArgumentException(JakdukUtils.getMessageSource("exception.invalid.parameter"));

		Map<String, Object> response = new HashMap<>();
		Optional<Token> oToken = commonService.getTokenByCode(code);

		if (! oToken.isPresent() || ! StringUtils.equals(oToken.get().getCode(), code))
			throw new IllegalArgumentException(JakdukUtils.getMessageSource("user.msg.reset.password.invalid"));

		Token token = oToken.get();
		response.put("subject", token.getEmail());
		response.put("code", token.getCode());

		return response;
	}

	// jakduk 비밀번호 재설정 처리.
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public Map<String, Object> updatePassword(
			@RequestParam(value = "password") String password,
			@RequestParam(value = "code") String code) {

		Optional<Token> oToken = commonService.getTokenByCode(code);

		Map<String, Object> response = new HashMap<>();
		String message;

		if (oToken.isPresent()) {
			Token token = oToken.get();
			userService.userPasswordUpdateByEmail(token.getEmail(), passwordEncoder.encode(password.trim()));
			message = JakdukUtils.getMessageSource("user.msg.success.change.password");
			response.put("subject", token.getEmail());

			tokenRepository.delete(token);
		} else {
			message = JakdukUtils.getMessageSource("user.msg.reset.password.invalid");
		}

		response.put("message", message);

		return response;
	}
}
