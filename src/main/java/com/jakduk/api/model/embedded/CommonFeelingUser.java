package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 6. 16.
 * @desc     : 게시판의 좋아요, 싫어요 등을 사용하는 사용자
 */

@Getter
@AllArgsConstructor
@Document
public class CommonFeelingUser {
	
	@Id
	private String id;
	
	private String userId;
	
	private String username;
}
