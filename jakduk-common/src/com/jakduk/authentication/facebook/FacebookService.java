package com.jakduk.authentication.facebook;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.client.RestOperations;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */
public class FacebookService {
	private Logger logger = Logger.getLogger(this.getClass());	
	private String profileUri;
	private RestOperations facebookRestTemplate;

	public void setProfileUri(String profileUri) {
		this.profileUri = profileUri;
	} 
	
	public void setFacebookRestTemplate(RestOperations facebookRestTemplate) {
		this.facebookRestTemplate = facebookRestTemplate;
	}
	
	public FacebookUser findUser() {
		
		//logger.debug("phjang user=" + facebookRestTemplate.getForObject(profileUri, Map.class));
		FacebookUser facebookUser = facebookRestTemplate.getForObject(profileUri, FacebookUser.class);
		
		if(logger.isInfoEnabled()) {
			logger.info(facebookUser);
		}
		
		return facebookUser;
	}

}
