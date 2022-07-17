package com.jakduk.api.model.elasticsearch;

import java.util.List;

import com.jakduk.api.model.embedded.ArticleItem;
import com.jakduk.api.model.embedded.CommonWriter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 8. 23.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EsComment {
	private String id;
	private ArticleItem article;
	private CommonWriter writer;
	private String content;
	private List<String> galleries;
}
