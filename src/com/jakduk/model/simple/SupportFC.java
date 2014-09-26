package com.jakduk.model.simple;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 26.
 * @desc     :
 */

@Document(collection = "footballClub")
public class SupportFC {
	
	@Id
	private String id;
	
	private String fcId;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFcId() {
		return fcId;
	}

	public void setFcId(String fcId) {
		this.fcId = fcId;
	}
	
	@Override
	public String toString() {
		return "SupportFC [id=" + id + ", fcId=" + fcId + "]";
	}
	
}
