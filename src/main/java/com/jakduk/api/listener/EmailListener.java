package com.jakduk.api.listener;

import com.jakduk.api.mail.EmailService;
import com.jakduk.api.model.rabbitmq.EmailPayload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

@Component
public class EmailListener {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EmailService emailService;

	@RabbitListener(queues = "${jakduk.rabbitmq.queues.email.binding-queue-name}")
	public void receive(EmailPayload emailPayload) throws MessagingException {

		switch (emailPayload.getType()) {
			case WELCOME:
				emailService.sendBulk(emailPayload);
				break;
			case RESET_PASSWORD:
				emailService.sendResetPassword(emailPayload);
				break;
			case BULK:
				emailService.sendBulk(emailPayload);
				break;
		}
	}

}
