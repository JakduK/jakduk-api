package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.BoardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 25.
* @desc     :
*/

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
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

	private BoardStatus status;
}
