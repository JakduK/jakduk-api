package com.jakduk.api.model.simple;

import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

@Getter
@Setter
@Document(collection = "boardFree")
public class BoardFreeSimple {
	
	@Id
	private String id;

	private Integer seq;

	private CommonWriter writer;
	
	private String subject;

}
