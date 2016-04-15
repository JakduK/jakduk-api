package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.jakduk.model.embedded.SocialInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private SocialInfo socialInfo;
	
	private List<Integer> roles;

	private String about;
	
	@DBRef
	private FootballClub supportFC;
	
}
