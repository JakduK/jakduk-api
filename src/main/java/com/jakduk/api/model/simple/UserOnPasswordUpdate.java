package com.jakduk.api.model.simple;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 28.
 * @desc     :
 */

@Data
@Document(collection = "user")
public class UserOnPasswordUpdate {

	private String id;
	
	private String password;
	
}
