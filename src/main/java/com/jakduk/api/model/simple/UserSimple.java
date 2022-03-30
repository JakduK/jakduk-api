package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_USER)
public class UserSimple {
	
	@Id
	private String id;
	private String username;
	private String about;

	public UserSimple() {
	}

	public UserSimple(String id, String username, String about) {
		this.id = id;
		this.username = username;
		this.about = about;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getAbout() {
		return about;
	}
}
