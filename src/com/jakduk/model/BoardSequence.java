package com.jakduk.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BoardSequence {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private Long seq;
	
	@NotNull
	private Integer name;

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}
	
	public Integer getName() {
		return name;
	}

	public void setName(Integer name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BoardSequence [id=" + id + ", seq=" + seq + ", name=" + name + "]"; 
	}
	

}
