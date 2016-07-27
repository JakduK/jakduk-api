package com.jakduk.model.simple;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 25.
* @desc     :
*/

@Getter
@NoArgsConstructor
@ToString
@Document(collection = "boardFree")
public class BoardFreeOnSearchComment {
	
	/**
	 * ID
	 */
	@Id
	private String id;

	/**
	 * 글 제목
	 */
	private String subject;
	
	/**
	 * 글 번호
	 */
	private Integer seq;

	public BoardFreeOnSearchComment(String id, Integer seq) {
		this.id = id;
		this.seq = seq;
	}
}
