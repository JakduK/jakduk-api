package com.jakduk.api.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document
public class Sequence {

	@Id
	private String id;
	private Integer seq = 1; // 글 번호
	@NotNull
	private String name; // 이름

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Sequence{" +
			"id='" + id + '\'' +
			", seq=" + seq +
			", name='" + name + '\'' +
			'}';
	}
}
