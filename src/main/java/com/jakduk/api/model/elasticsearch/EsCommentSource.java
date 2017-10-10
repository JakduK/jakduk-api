package com.jakduk.api.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 2.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EsCommentSource {

	private String id;
	private EsParentArticle article;
	private CommonWriter writer;
	private String content;
	private Float score;
	private Map<String, List<String>> highlight;
}
