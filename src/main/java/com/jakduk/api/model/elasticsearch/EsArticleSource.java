package com.jakduk.api.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@NoArgsConstructor
@Getter
@Setter
public class EsArticleSource {

	private String id;
	private Integer seq;
	private String board;
	private String category;
	private CommonWriter writer;
	private String subject;
	private String content;
	private List<String> galleries;
	private Float score;
	private Map<String, List<String>> highlight;

}
