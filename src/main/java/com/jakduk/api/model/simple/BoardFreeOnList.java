package com.jakduk.api.model.simple;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.model.embedded.BoardStatus;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     : 각종 목록에서 쓰임
 */

@Getter
@Document(collection = "boardFree")
public class BoardFreeOnList {
	
	@Id
	private String id;

	private CommonWriter writer;
	
	private String subject;
	
	private int seq;
	
	private JakdukConst.BOARD_CATEGORY_TYPE category;
	
	private int views = 0;
	
	private BoardStatus status;

	private String shortContent;

	private boolean linkedGallery;

}
