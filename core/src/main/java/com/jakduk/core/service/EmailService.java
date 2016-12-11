package com.jakduk.core.service;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.Token;
import com.jakduk.core.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
public class EmailService {

	@Value("${email.enable}")
	private boolean emailEnabled;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine htmlTemplateEngine;

	@Async(value = "asyncMailExecutor")
	public void sendResetPassword(final Locale locale, final String host, final String recipientEmail)
			throws MessagingException {

		if (! emailEnabled) return;

		if (log.isDebugEnabled()) {
			log.debug("send email to reset password. email is " + recipientEmail);
		}

		String code = UUID.randomUUID().toString();
		String callbackUrl = host + "/" + code;

		ResourceBundle bundle = ResourceBundle.getBundle("messages.user", locale);
		String language = CoreUtils.getLanguageCode(locale, null);

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("email", recipientEmail);
		ctx.setVariable("host", callbackUrl);
		ctx.setVariable("linkLabel", bundle.getString("user.password.change"));

		String subject = "jakduk.com-" + bundle.getString("user.password.reset.instructions");

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message
				= new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
		message.setSubject(subject);
//		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.htmlTemplateEngine.process("resetPassword", ctx);
		message.setText(htmlContent, true /* isHtml */);

		String logoPath = "";

		if (language.equals(Locale.KOREAN.getLanguage())) {
			logoPath = "mail/images/logo_type_A_kr.png";
		} else {
			logoPath = "mail/images/logo_type_A_en.png";
		}

		message.addInline("logo", new ClassPathResource(logoPath), "image/png");

		// Send mail
		this.mailSender.send(mimeMessage);

		Token token = new Token();
		token.setEmail(recipientEmail);
		token.setCode(code);
		token.setExpireAt(
				Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant())
		);

		if (Objects.nonNull(tokenRepository.findOne(recipientEmail))) {
			tokenRepository.delete(recipientEmail);
		}

		tokenRepository.insert(token);
	}

	@Async(value = "asyncMailExecutor")
	public void sendWelcome(final Locale locale, final String username, final String recipientEmail) {

		if (! emailEnabled) return;

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("username", username);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

		try {
			message.setSubject("K리그 작두왕에 오신것을 환영합니다.");
			message.setTo(recipientEmail);

			// Create the HTML body using Thymeleaf
			final String htmlContent = this.htmlTemplateEngine.process("welcome", ctx);
			message.setText(htmlContent, true /* isHtml */);

			// Send email
			this.mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.error(e.getLocalizedMessage());
		}
	}

	/**
	 * Send HTML mail with inline image
	 */
	public void sendMailWithInline(final String recipientName, final String recipientEmail, final Locale locale)
			throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariable("name", recipientName);
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message
				= new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
		message.setSubject("Example HTML email with inline image");
		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.htmlTemplateEngine.process("email-inlineimage", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
//		final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
//		message.addInline(imageResourceName, imageSource, imageContentType);
		message.addInline("sample-image", new ClassPathResource("mail/images/logo_type_A_en.png"), "image/png");

		// Send mail
		this.mailSender.send(mimeMessage);
	}

}
