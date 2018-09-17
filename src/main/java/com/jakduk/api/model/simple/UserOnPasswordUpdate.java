package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 28.
 * @desc     :
 */

@Document(collection = Constants.COLLECTION_USER)
public class UserOnPasswordUpdate {
	private String id;
	private String password;

	public UserOnPasswordUpdate() {
	}

	public UserOnPasswordUpdate(String id, String password) {
		this.id = id;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
