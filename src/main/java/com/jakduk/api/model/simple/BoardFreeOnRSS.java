package com.jakduk.api.model.simple;

import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 24.
 * @desc     :
 */

@Getter
@Document(collection = "boardFree")
public class BoardFreeOnRSS {

	private String id;
	
	private Integer seq;

	private CommonWriter writer;
	
	private String subject;
	
	private String content;

}
