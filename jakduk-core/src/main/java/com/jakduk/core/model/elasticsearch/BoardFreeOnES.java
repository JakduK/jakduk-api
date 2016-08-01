package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;
import lombok.Data;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 3.
* @desc     :
*/

@Data
public class BoardFreeOnES {
	
	@JestId
    private String id;
	
	private CommonWriter writer;
	
	private String subject;
	
	private String content;
	
	private int seq;
	
	private String categoryName;
}
