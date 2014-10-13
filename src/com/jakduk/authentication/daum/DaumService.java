package com.jakduk.authentication.daum;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestOperations;

import com.jakduk.authentication.facebook.FacebookUser;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */
public class DaumService {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String profileUri;
	private RestOperations daumRestTemplate;
	
	public void setProfileUri(String profileUri) {
		this.profileUri = profileUri;
	} 
	
	public void setDaumRestTemplate(RestOperations daumRestTemplate) {
		this.daumRestTemplate = daumRestTemplate;
	}
	
	public void findUser() {
		
		logger.debug("phjang=" + daumRestTemplate.getForObject(profileUri, Map.class));
	}
	

}
