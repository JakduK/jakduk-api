package com.jakduk.api.model.aggregate;

import com.jakduk.api.model.embedded.BoardStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardPostTop {

	private String id;
	private Integer seq;
	private BoardStatus status;
	private String subject;
	private Integer count;
	private Integer views;

}
