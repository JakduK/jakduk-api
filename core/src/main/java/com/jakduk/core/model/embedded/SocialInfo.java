package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CommonConst;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 8. 31.
 * @desc     :
 */

@Document
@Data
public class SocialInfo {
	
	/**
	 * OAuth 프로바이더 종류이다. CommonConst클래스의 OAUTH_TYPE_xxx 와 대응된다.
	 */
	private CommonConst.ACCOUNT_TYPE providerId;
	private String oauthId;
}
