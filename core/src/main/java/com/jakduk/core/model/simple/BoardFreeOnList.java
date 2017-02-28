package com.jakduk.core.model.simple;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.embedded.BoardImage;
import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */

@Getter
@Document(collection = "boardFree")
public class BoardFreeOnList {
	
	@Id
	private String id;

	/**
	 * 작성자
	 */
	private CommonWriter writer;
	
	/**
	 * 글 제목
	 */
	private String subject;
	
	/**
	 * 글 번호
	 */
	private int seq;
	
	/**
	 * 분류 ID
	 */
	private CoreConst.BOARD_CATEGORY_TYPE category;
	
	/**
	 * 조회
	 */
	private int views = 0;
	
	private BoardStatus status;
	
	private List<BoardImage> galleries;
}
