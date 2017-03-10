package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 26.
 * @desc     :
 */

// BoardFreeOnList와 통합 시키자.
@Deprecated
@NoArgsConstructor
@Getter
@Document(collection = "boardFree")
public class BoardFreeSimple {
	
	@Id
	private String id;

	/**
	 * 글 번호
	 */
	private Integer seq;

	/**
	 * 작성자
	 */
	private CommonWriter writer;
	
	/**
	 * 글 제목
	 */
	private String subject;
}
