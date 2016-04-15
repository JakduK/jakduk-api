package com.jakduk.model.web;

import com.jakduk.model.embedded.LocalName;
import lombok.Data;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 2.
 * @desc     :
 */

@Data
public class OAuthProfileInfo {
	
	private String username;
	
	private String about;
	
	private LocalName footballClubName;
}
