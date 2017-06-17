package com.jakduk.core.service;

import com.jakduk.core.common.util.CoreUtils;
import com.jakduk.core.model.db.Token;
import com.jakduk.core.model.rabbitmq.EmailPayload;
import com.jakduk.core.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class CommonEmailService {

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine htmlTemplateEngine;

	public void sendResetPassword(EmailPayload emailPayload) throws MessagingException {

		Locale locale = emailPayload.getLocale();
		String recipientEmail = emailPayload.getRecipientEmail();

		String code = UUID.randomUUID().toString();
		String callbackUrl = emailPayload.getExtra().get("host") + "/" + code;

		String language = CoreUtils.getLanguageCode(locale, null);

		// Prepare the evaluation context
		final Context ctx = new Context(locale);
		ctx.setVariables(emailPayload.getBody());
		ctx.setVariable("callbackUrl", callbackUrl);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message
				= new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
		message.setSubject(emailPayload.getSubject());
//		message.setFrom("thymeleaf@example.com");
		message.setTo(recipientEmail);

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.htmlTemplateEngine.process("mail/resetPassword", ctx);
		message.setText(htmlContent, true /* isHtml */);

		String logoPath = "";

		if (language.equals(Locale.KOREAN.getLanguage())) {
			logoPath = "public/images/logo_type_A_kr.png";
		} else {
			logoPath = "public/images/logo_type_A_en.png";
		}

		message.addInline("logo", new ClassPathResource(logoPath), "image/png");

		// Send mail
		this.mailSender.send(mimeMessage);

		Optional<Token> optToken = tokenRepository.findOneByEmail(recipientEmail);

		if (optToken.isPresent()) {
			optToken.ifPresent(token -> {
				token.setCode(code);
				token.setExpireAt(
						Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant())
				);

				tokenRepository.save(token);
			});
		} else {
			Token token = Token.builder()
					.email(recipientEmail)
					.code(code)
					.expireAt(
							Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant())
					)
					.build();

			tokenRepository.save(token);
		}
	}

	public void sendWelcome(EmailPayload emailPayload) throws MessagingException {

		// Prepare the evaluation context
		final Context ctx = new Context(emailPayload.getLocale());
		ctx.setVariables(emailPayload.getBody());

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");

		message.setSubject(emailPayload.getSubject());
		message.setTo(emailPayload.getRecipientEmail());

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.htmlTemplateEngine.process("mail/welcome", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);
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
		final String htmlContent = this.htmlTemplateEngine.process("mail/email-inlineimage", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
//		final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
//		message.addInline(imageResourceName, imageSource, imageContentType);
		message.addInline("sample-image", new ClassPathResource("public/images/logo_type_A_en.png"), "image/png");

		// Send mail
		this.mailSender.send(mimeMessage);
	}

}
