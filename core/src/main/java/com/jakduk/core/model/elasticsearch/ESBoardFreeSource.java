package com.jakduk.core.model.elasticsearch;

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
public class ESBoardFreeSource {
	private String id;

	private CommonWriter writer;

	private String subject;

	private String contentPreview;

	private Integer seq;

	private String category;

	private Float score;

	private Map<String, List<String>> highlight;
}
