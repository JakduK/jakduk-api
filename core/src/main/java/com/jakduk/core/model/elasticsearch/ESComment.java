package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.*;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 23.
* @desc     :
*/

@Builder
@Getter
@Setter
public class ESComment {
	
    private String id;
	
	private BoardItem boardItem;
	
	private CommonWriter writer;
	
	private String content;
}
