package com.jakduk.core.model.elasticsearch;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

@Builder
@Getter
public class ESTermsBucket {

	private String key;
	private Long count;
}
