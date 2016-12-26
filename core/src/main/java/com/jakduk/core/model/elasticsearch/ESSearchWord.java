package com.jakduk.core.model.elasticsearch;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jakduk.core.common.util.json.LocalDateTimeSerializer;
import com.jakduk.core.model.embedded.CommonWriter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 26.
 */

@Builder
@Getter
@Setter
public class ESSearchWord {

	private String id;

	private String word;

	private CommonWriter writer;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime registerDate;
}
