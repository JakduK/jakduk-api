package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.BoardStatus;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@Getter
@Document(collection = "boardFree")
public class BoardFreeOnHome {
	
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
	
	private BoardStatus status;
	
	private int views;

	private String shortContent;
	
}
