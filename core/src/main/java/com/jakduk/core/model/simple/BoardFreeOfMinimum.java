package com.jakduk.core.model.simple;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 11. 17.
 * @desc     :
 */

@Data
@Document(collection = "boardFree")
public class BoardFreeOfMinimum {
	
	@Id
	private String id;
	
	private int seq;
}
