package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.db.FootballClub;
import com.jakduk.api.model.db.UserPicture;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 6.
 * @desc     : User 객체에서 password와 같은 민감한 필드가 빠짐
 */

@Document(collection = Constants.COLLECTION_USER)
public class UserProfile {

	@Id
	private String id;
	private String email;
	private String username;
	private Constants.ACCOUNT_TYPE providerId;        // 제공자
	private String providerUserId;                    // SNS USER ID
	private String about;
	@DBRef
	private FootballClub supportFC;
	@DBRef
	private UserPicture userPicture;                // 프로필 사진

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public Constants.ACCOUNT_TYPE getProviderId() {
		return providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public String getAbout() {
		return about;
	}

	public FootballClub getSupportFC() {
		return supportFC;
	}

	public UserPicture getUserPicture() {
		return userPicture;
	}
}
