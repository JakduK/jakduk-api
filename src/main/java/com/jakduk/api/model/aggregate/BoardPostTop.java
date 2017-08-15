package com.jakduk.api.model.aggregate;

import com.jakduk.api.model.embedded.BoardStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BoardPostTop {

	private String id;
	private Integer seq;
	private BoardStatus status;
	private String subject;
	@Setter private Integer count;
	private Integer views;

}
