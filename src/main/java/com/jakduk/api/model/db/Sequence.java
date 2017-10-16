package com.jakduk.api.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@Document
@ToString
public class Sequence {
	
	@Id
	private String id;
	private Integer seq = 1; // 글 번호
	@NotNull
	private String name; // 이름

}
