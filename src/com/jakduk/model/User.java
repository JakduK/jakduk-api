package com.jakduk.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	@Size(min = 1, message="Input userID")
	private String principle;
	
	@NotNull
	@Size(min = 1, message="Input userName")
	private String userName;
	
	@NotNull
	@Size(min = 4, message="Input password")
	private String password;
	
	@Temporal(TemporalType.DATE)
	private Date joined;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPrinciple() {
		return principle;
	}

	public void setPrinciple(String principle) {
		this.principle = principle;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getJoined() {
		return joined;
	}

	public void setJoined(Date joined) {
		this.joined = joined;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", principle=" + principle + ", userName=" + userName + ", password=" + password + ", joined=" + joined + "]"; 
	}
	
}
