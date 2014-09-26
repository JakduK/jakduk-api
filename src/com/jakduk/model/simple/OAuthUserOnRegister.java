package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 26.
 * @desc     :
 */

@Document(collection = "user")
public class OAuthUserOnRegister {
	
	@Id
	private String id;
	
	private String about;
	
	@DBRef
	private SupportFC supportFC;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}
	
	public SupportFC getSupportFC() {
		return supportFC;
	}

	public void setSupportFC(SupportFC supportFC) {
		this.supportFC = supportFC;
	}

	@Override
	public String toString() {
		return "OAuthUserOnRegister [id=" + id + ", about=" + about
				+ ", supportFC=" + supportFC + "]";
	}

}
