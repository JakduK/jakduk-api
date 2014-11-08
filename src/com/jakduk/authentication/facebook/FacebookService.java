package com.jakduk.authentication.facebook;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestOperations;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */

public class FacebookService {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private String profileUri;
	private RestOperations facebookRestTemplate;

	public void setProfileUri(String profileUri) {
		this.profileUri = profileUri;
	} 
	
	public void setFacebookRestTemplate(RestOperations facebookRestTemplate) {
		this.facebookRestTemplate = facebookRestTemplate;
	}
	
	public FacebookUser findUser() {
		
		FacebookUser facebookUser = 	facebookRestTemplate.getForObject(profileUri, FacebookUser.class);
		
		if(LOGGER.isInfoEnabled()) {
			LOGGER.info(facebookUser.toString());
		}
		
		return facebookUser;
	}

}
