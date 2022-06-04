package com.jakduk.api.model.simple;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 24.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleOnRSS {

	private String id;
	private String board;
	private Integer seq;
	private CommonWriter writer;
	private String subject;
	private String content;

}
