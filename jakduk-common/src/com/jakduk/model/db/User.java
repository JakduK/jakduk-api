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
	
	private String email;							// 이메일
	
	private String username;						// 별명
	
	private String password;						// 비밀번호

	private CommonConst.ACCOUNT_TYPE providerId;	// 제공자

	private String providerUserId;					// SNS USER ID

	private List<Integer> roles;					// 권한

	private String about;							// 소개
	
	@DBRef
	private FootballClub supportFC;					// 지지구단
	
}
