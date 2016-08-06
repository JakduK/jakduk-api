package com.jakduk.core.model.simple;

import com.jakduk.core.model.db.FootballClub;
import com.jakduk.core.model.embedded.SocialInfo;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 2.
 * @desc     :
 */

@Data
public class OAuthProfile {
	
	private String id;
	
	private String username;
	
	private SocialInfo oauthUser;
	
	private List<Integer> roles;
	
	private String about;
	
	@DBRef
	private FootballClub supportFC;

}
