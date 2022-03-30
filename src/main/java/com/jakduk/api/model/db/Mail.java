package com.jakduk.api.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Mail {

	@Id
	private String id;
	private String subject; // 제목
	private String templateName; // 템플릿 이름

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	@Override
	public String toString() {
		return "Mail{" +
			"id='" + id + '\'' +
			", subject='" + subject + '\'' +
			", templateName='" + templateName + '\'' +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Mail mail = (Mail)o;
		return Objects.equals(id, mail.id) &&
			Objects.equals(subject, mail.subject) &&
			Objects.equals(templateName, mail.templateName);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id, subject, templateName);
	}
}
