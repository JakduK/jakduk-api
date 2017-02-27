package com.jakduk.core.model.db;

import com.jakduk.core.common.CoreConst;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class User {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String email;							// 이메일
	
	private String username;						// 별명
	
	private String password;						// 비밀번호

	private CoreConst.ACCOUNT_TYPE providerId;		// 제공자

	private String providerUserId;					// SNS USER ID

	private List<Integer> roles;					// 권한

	private String about;							// 소개

	@DBRef
	private FootballClub supportFC;					// 지지구단

	@DBRef
	private UserPicture userPicture;					// 프로필 사진

}
