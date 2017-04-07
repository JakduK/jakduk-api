package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

// BoardFreeSimple 로 대체하자.
@Deprecated
@Getter
@Document(collection = "boardFree")
public class BoardFreeSimple {
	
	@Id
	private String id;

	private Integer seq;

	private CommonWriter writer;
	
	private String subject;
}
