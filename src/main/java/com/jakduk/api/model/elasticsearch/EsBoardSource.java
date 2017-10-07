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
public class EsBoardSource {

	private String id;

	private String board;

	private CommonWriter writer;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String subject;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String content;

	private Integer seq;

	private String category;

	private List<String> galleries;

	private Float score;

	private Map<String, List<String>> highlight;

}
