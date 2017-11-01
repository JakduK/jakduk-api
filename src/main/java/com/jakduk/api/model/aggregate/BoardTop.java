package com.jakduk.api.model.aggregate;

import com.jakduk.api.model.embedded.ArticleStatus;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardTop {
	private String id;
	private Integer seq;
	private ArticleStatus status;
	private String subject;
	private Integer count;
	private Integer views;
}
