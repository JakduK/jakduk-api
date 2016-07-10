package com.jakduk.authentication.facebook;

import java.util.Map;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestOperations;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 27.
 * @desc     :
 */

@Slf4j
public class FacebookService {

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
		
		if(log.isInfoEnabled()) {
			log.info("facebookuser", facebookUser);
		}
		
		return facebookUser;
	}

}
