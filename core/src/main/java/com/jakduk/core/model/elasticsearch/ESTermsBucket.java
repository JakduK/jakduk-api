package com.jakduk.core.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jang, Pyohwan(1100273)
 * @since 2016. 12. 26.
 */

@Builder
@Getter
public class ESTermsBucket {

	private String key;
	private Long count;
}
