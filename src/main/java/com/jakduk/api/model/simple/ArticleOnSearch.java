package com.jakduk.api.model.simple;

import com.jakduk.api.model.embedded.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class ArticleOnSearch {
	
	private String id;

	private Integer seq;

	private String subject;

	private ArticleStatus status;

}
