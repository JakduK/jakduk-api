package com.jakduk.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Document
public class Token {

	@Id
	private String email;

	private String code;
	private Type tokenType = Type.RESET_PASSWORD;

	@Temporal(TemporalType.DATE)
	private Date createdTime;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Type getTokenType() {
		return tokenType;
	}

	public void setTokenType(Type tokenType) {
		this.tokenType = tokenType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public enum Type {
		RESET_PASSWORD
	}
}
