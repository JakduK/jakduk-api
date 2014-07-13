package com.jakduk.model.simple;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 게시판 작성자
 * @author pyohwan
 *
 */
@Document(collection = "user")
public class BoardWriter {
	
	@Id
	private String id;
	
	@NotNull
	private String username;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
