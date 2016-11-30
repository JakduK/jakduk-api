package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 3.
* @desc     :
*/

@Getter
@Setter
@Builder
public class ESBoardFree {
	
    private String id;
	
	private CommonWriter writer;
	
	private String subject;
	
	private String content;
	
	private Integer seq;
	
	private String category;
}
