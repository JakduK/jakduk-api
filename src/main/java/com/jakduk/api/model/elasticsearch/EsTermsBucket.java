package com.jakduk.api.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EsTermsBucket {

	private String key;
	private Long count;
}
