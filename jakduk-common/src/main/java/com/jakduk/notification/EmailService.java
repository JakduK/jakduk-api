package com.jakduk.notification;

import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.service.CommonService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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
	private JavaMailSender mailSender;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${smtp.username}")
	private String fromMail;

	@Value("${email.url.static.resource}")
	private String staticResourceUrl;

	@Async(value = "asyncMailExecutor")
	public void sendResetPassword(String host, String email) {

		if (log.isDebugEnabled()) {
			log.debug("send email to reset password. email is " + email);
		}

		Locale locale = LocaleContextHolder.getLocale();
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

			String subject = "jakduk.com-" + bundle.getString("user.password.reset.instructions");

			StringBuilder content = new StringBuilder();

			try {
				Template template = getTemplate("resetPassword.ftl");
				content.append(FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
			} catch (TemplateException e) {
				e.printStackTrace();
			}

			send(email, subject, content.toString());

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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Template getTemplate(String templateName) throws IOException {
		Configuration cfg = freeMarkerConfigurer.getConfiguration();

		return cfg.getTemplate(templateName);
	}

	private void send(String to, String subject, String htmlBody) throws MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(htmlBody, true);

		mailSender.send(message);
	}
}
