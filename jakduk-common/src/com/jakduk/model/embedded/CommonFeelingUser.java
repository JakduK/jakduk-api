package com.jakduk.model.embedded;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 16.
 * @desc     : 게시판의 좋아요, 싫어요 등을 사용하는 사용자
 */

@Document
@Data
public class CommonFeelingUser {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String userId;
	
	private String username;
}
