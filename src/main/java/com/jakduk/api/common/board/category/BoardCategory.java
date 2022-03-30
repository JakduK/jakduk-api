package com.jakduk.api.common.board.category;

import com.jakduk.api.model.embedded.LocalSimpleName;

import java.util.List;

public class BoardCategory {

	private String code;
	private List<LocalSimpleName> names;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<LocalSimpleName> getNames() {
		return names;
	}

	public void setNames(List<LocalSimpleName> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "BoardCategory{" +
				"code='" + code + '\'' +
				", names=" + names +
				'}';
	}
}
