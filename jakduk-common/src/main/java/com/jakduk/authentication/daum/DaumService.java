package com.jakduk.authentication.daum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestOperations;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 11.
 * @desc     :
 */

@Slf4j
public class DaumService {

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
		
		if (log.isInfoEnabled()) {
			log.info("user", user);
		}
		
		return user;
	}
	

}
