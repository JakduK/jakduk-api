package com.jakduk.service;

import com.jakduk.model.db.Token;
import com.jakduk.repository.TokenRepository;
import com.jakduk.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

@Service
public class EmailService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private JavaMailSender sender;

	@Value("${smtp.username}")
	private String fromMail;

	@Value("${host.reset.password}")
	private String hostResetPassword;

	public void sendResetPassword(String email) {
		if (Objects.isNull(userRepository.findByEmail(email))) {
			return;
		}

		try {
			String code = UUID.randomUUID().toString();
			ResourceBundle bundle = ResourceBundle.getBundle("messages.user");
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(fromMail);
			helper.setTo(email);
			helper.setSubject("jakduk.com - " + bundle.getString("user.password.reset.instructions"));
			helper.setText(
				"<html>" +
					"<body>" +
						"<a href=\"" + hostResetPassword + "?code=" + code + "\" target=\"_blank\">" + bundle.getString("user.password.change") + "</a>" +
					"</body>" +
				"</html>", true
			);
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
			logger.error(e);
		}
	}
}
