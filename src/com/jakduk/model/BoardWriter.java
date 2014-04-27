package com.jakduk.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class BoardWriter {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	@Size(min = 1, message="Input userName")
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "BoardWriter [id=" + id + ", username=" + username + "]"; 
	}

}
