package com.jakduk.api.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Document
public class Sequence {
	
	@Id
	private String id;
	
	/**
	 * 글 번호
	 */
	private Integer seq = 1;
	
	/**
	 * 게시판 ID
	 * JakdukConst 의 게시판 ID 참고
	 */
	@NotNull
	private String name;

}
