package com.jakduk.api.model.elasticsearch;

import com.jakduk.api.model.embedded.CommonWriter;
import com.jakduk.api.model.embedded.SimpleWriter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Jang, Pyohwan
 * @since 2016. 12. 26.
 */

@Builder
@Getter
@Setter
public class EsSearchWord {

	private String id;
	private String word;
	private SimpleWriter writer;
	private LocalDateTime registerDate;

}
