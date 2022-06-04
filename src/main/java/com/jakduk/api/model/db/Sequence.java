package com.jakduk.api.model.db;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document
public class Sequence {

	@Id
	private String id;
	private Integer seq; // 글 번호
	@NotNull
	private String name; // 이름

}
