package com.jakduk.core.model.simple;

import com.jakduk.core.model.embedded.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 8. 25.
* @desc     :
*/

@NoArgsConstructor
@Getter
public class BoardFreeOnSearch {
	
	private String id;

	private String subject;
	
	private Integer seq;

	private BoardStatus status;
}
