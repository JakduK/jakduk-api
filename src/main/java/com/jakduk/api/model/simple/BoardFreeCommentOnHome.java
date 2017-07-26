package com.jakduk.api.model.simple;

import com.jakduk.api.model.embedded.BoardItem;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 27.
 * @desc     :
 */

@Data
@Document(collection = "boardFreeComment")
public class BoardFreeCommentOnHome {
	
	@Id
	private String id;
	
	private BoardItem boardItem;
	
	private CommonWriter writer;
	
	private String content;
}
