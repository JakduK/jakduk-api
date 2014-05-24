package com.jakduk.model.web;

import com.jakduk.common.CommonConst;

public class BoardListInfo {

	private int page = 0;
	private int category = CommonConst.BOARD_CATEGORY_NONE;

	public Integer getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "BoardListInfo [page=" + page + ", category=" + category + "]";
	}

}
