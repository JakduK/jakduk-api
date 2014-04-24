package com.jakduk.model;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BoardCategory {

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@NotNull
	@Size(min = 1, message="Input Name")
	private String name;
	
	private List<Integer> usingBoard;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getUsingBoard() {
		return usingBoard;
	}

	public void setUsingBoard(List<Integer> usingBoard) {
		this.usingBoard = usingBoard;
	}
	
	@Override
	public String toString() {
		return "BoardCategory [id=" + id + ", name=" + name + "]";
	}
	
	
}
