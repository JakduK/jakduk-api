package com.jakduk.core.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 2.
 */

@NoArgsConstructor
@Getter
@Setter
public class ESCommentSource {

	private String id;

	private ESParentBoard parentBoard;

	private CommonWriter writer;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private String content;

	private Float score;

	private Map<String, List<String>> highlight;
}
