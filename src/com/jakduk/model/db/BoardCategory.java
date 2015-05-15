package com.jakduk.model.db;

import java.io.Serializable;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BoardCategory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7181142801221366583L;

	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;
	
	private String resName;
	
	private List<String> usingBoard;

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

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public List<String> getUsingBoard() {
		return usingBoard;
	}

	public void setUsingBoard(List<String> usingBoard) {
		this.usingBoard = usingBoard;
	}

	@Override
	public String toString() {
		return "BoardCategory [id=" + id + ", name=" + name + ", resName="
				+ resName + ", usingBoard=" + usingBoard + "]";
	}
	
}
