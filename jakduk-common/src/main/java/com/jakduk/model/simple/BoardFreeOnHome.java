package com.jakduk.model.simple;

import com.jakduk.model.embedded.BoardStatus;
import com.jakduk.model.embedded.CommonWriter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 2.
 * @desc     :
 */

@Data
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
	
}
