package com.jakduk.model.simple;

import java.util.List;

import com.jakduk.common.CommonConst;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.BoardImage;
import com.jakduk.model.embedded.BoardStatus;
import com.jakduk.model.embedded.CommonWriter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 13.
 * @desc     :
 */

@Data
@Document(collection = "boardFree")
public class BoardFreeOnList {
	
	/**
	 * ID
	 */
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
	private CommonConst.BOARD_CATEGORY_TYPE category;
	
	/**
	 * 조회
	 */
	private int views = 0;
	
	private BoardStatus status;
	
	private List<BoardImage> galleries;
}
