package com.jakduk.api.model.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Getter
@Setter
@Document
public class Encyclopedia {

	@Id
	private String id;
	private Integer seq;
	private String kind; // 종류. 1이면 최고의 선수, 2이면 추천 책
	private String language;
	private String subject;
	private String content;

}
