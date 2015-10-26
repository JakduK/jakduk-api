package com.jakduk.authentication.daum;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestOperations;

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
	
	public DaumUser findUser() {
		
//		logger.debug("phjang user=" + daumRestTemplate.getForObject(profileUri, Map.class));
		DaumUser user = daumRestTemplate.getForObject(profileUri, DaumUser.class);
		
		if (logger.isInfoEnabled()) {
			logger.info(user);
		}
		
		return user;
	}
	

}
