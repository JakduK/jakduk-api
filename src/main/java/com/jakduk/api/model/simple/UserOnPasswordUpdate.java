package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 10. 28.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document(collection = Constants.COLLECTION_USER)
public class UserOnPasswordUpdate {
	private String id;
	private String password;
}
