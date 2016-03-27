package com.jakduk.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.repository.UserRepository;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${smtp.username}")
	private String fromMail;

	@Value("${email.url.reset.password}")
	private String resetPasswordPath;

	@Value("${email.url.static.resource}")
	private String staticResourceUrl;

	public void sendResetPassword(String host, Locale locale, String email) {
		if (Objects.isNull(userRepository.findByEmail(email))) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("send email to reset password. email is " + email);
		}

		try {
			String code = UUID.randomUUID().toString();
			ResourceBundle bundle = ResourceBundle.getBundle("messages.user", locale);
			String lang = bundle.getLocale().getLanguage();
			if ("ko".equals(lang)) {
				lang = "kr";
			} else {
				lang = "en";
			}

			Map<String, Object> model = new HashMap<>();
			model.put("lang", lang);
			model.put("title", "JakduK - " + bundle.getString("user.password.reset.instructions"));
			model.put("logo", host + staticResourceUrl + "/img/logo_type_A_" + lang + ".png");
			model.put("host", host + resetPasswordPath);
			model.put("code", code);
			model.put("greeting", new MessageFormat(bundle.getString("user.password.reset.greeting")).format(new String[]{email}));
			model.put("linkLabel", bundle.getString("user.password.change"));

			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(fromMail);
			helper.setTo(email);
			helper.setSubject("jakduk.com - " + bundle.getString("user.password.reset.instructions"));
			helper.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/resetPassword.vm", "UTF-8", model), true);

			sender.send(message);

			Token token = new Token();
			token.setEmail(email);
			token.setCode(code);
			token.setCreatedTime(new Date());
			if (Objects.nonNull(tokenRepository.findOne(email))) {
				tokenRepository.delete(email);
			}
			tokenRepository.insert(token);
		} catch (MessagingException e) {
			log.error("error", e);
		}
	}
}
