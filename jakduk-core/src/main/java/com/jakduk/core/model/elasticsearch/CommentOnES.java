package com.jakduk.core.model.elasticsearch;

import com.jakduk.core.model.embedded.BoardItem;
import com.jakduk.core.model.embedded.CommonWriter;
import io.searchbox.annotations.JestId;
import lombok.Data;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 23.
* @desc     :
*/

@Data
public class CommentOnES {
	
	@JestId
    private String id;
	
	private BoardItem boardItem;
	
	private CommonWriter writer;
	
	private String content;
}
