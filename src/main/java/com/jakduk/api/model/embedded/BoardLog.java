package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

@AllArgsConstructor
@Setter
@Getter
public class BoardLog {
	
	@Id
	private String id;
	
	private String type;

	private SimpleWriter writer;
	
}
