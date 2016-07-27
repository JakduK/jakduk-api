package com.jakduk.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 25.
 * @desc     :
 */

@AllArgsConstructor
@Getter
public class BoardImage {
	private String id;
}
