package com.jakduk.model.simple;

import java.util.List;

import com.jakduk.model.embedded.SocialInfo;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 12.
 * @desc     :
 */

@Data
public class SocialUserOnLogin {
	
	@Id
	private String id;

	private String email;
	
	private String username;
	
	private SocialInfo socialInfo;
	
	private List<Integer> roles;

}
