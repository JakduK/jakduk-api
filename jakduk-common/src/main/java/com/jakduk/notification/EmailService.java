package com.jakduk.notification;

import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Service
public class EmailService {

	@Autowired
	private CommonService commonService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private VelocityEngine velocityEngine;

	@Value("${smtp.username}")
	private String fromMail;

	@Value("${email.url.static.resource}")
	private String staticResourceUrl;

	public void sendResetPassword(Locale locale, String host, String email) {

		if (log.isDebugEnabled()) {
			log.debug("send email to reset password. email is " + email);
		}

		String language = commonService.getLanguageCode(locale, null);

		try {
			String code = UUID.randomUUID().toString();
			ResourceBundle bundle = ResourceBundle.getBundle("messages.user", locale);

			String logoPath = staticResourceUrl;

			if (language.equals(Locale.KOREAN.getLanguage())) {
				logoPath += "/img/logo_type_A_kr.png";
			} else {
				logoPath = "/img/logo_type_A_en.png";
			}

			Map<String, Object> model = new HashMap<>();
			model.put("lang", language);
			model.put("title", "JakduK - " + bundle.getString("user.password.reset.instructions"));
			model.put("logo", logoPath);
			model.put("host", host);
			model.put("code", code);
			model.put("greeting", new MessageFormat(bundle.getString("user.password.reset.greeting")).format(new String[]{email}));
			model.put("linkLabel", bundle.getString("user.password.change"));

			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(fromMail);
			helper.setTo(email);
			helper.setSubject("jakduk.com-" + bundle.getString("user.password.reset.instructions"));
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
