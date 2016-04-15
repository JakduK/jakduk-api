package com.jakduk.model.db;

import com.jakduk.common.CommonConst;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Data
@Document
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String email;
	
	private String username;
	
	private String password;

	private CommonConst.ACCOUNT_TYPE providerId;

	private String providerUserId;

	private List<Integer> roles;

	private String about;
	
	@DBRef
	private FootballClub supportFC;
	
}
