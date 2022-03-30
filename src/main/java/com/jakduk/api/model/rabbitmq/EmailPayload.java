package com.jakduk.api.model.rabbitmq;

import com.jakduk.api.common.Constants;

import java.util.Locale;
import java.util.Map;

/**
 * Created by pyohwanjang on 2017. 6. 17..
 */

public class EmailPayload {

	private Locale locale;
	private Constants.EMAIL_TYPE type;
	private String templateName;
	private String recipientEmail;
	private String subject;
	private Map<String, String> extra;
	private Map<String, Object> body;

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Constants.EMAIL_TYPE getType() {
		return type;
	}

	public void setType(Constants.EMAIL_TYPE type) {
		this.type = type;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
}
