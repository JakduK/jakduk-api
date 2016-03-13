package com.jakduk.model.embedded;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 30.
 * @desc     :
 */

@Document
@Data
public class BoardItem {
	
	private String id;
	
	private int seq;
}
