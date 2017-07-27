package com.jakduk.api.model.db;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 7. 4.
 * @desc     :
 */

@Data
@Document
public class Encyclopedia {
	
	@Id
	private String id;
	
	/**
	 *  종류. 1이면 최고의 선수, 2이면 추천 책
	 */
	private String kind;
	
	private String language;
	
	@NotNull
	private String subject;
	
	@NotNull
	private String content;
	
	private int seq;
}
