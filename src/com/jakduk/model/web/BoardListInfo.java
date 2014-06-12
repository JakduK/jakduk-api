package com.jakduk.model.web;

import com.jakduk.common.CommonConst;

/**
 * 게시판의 현재 페이지와 분류에 대한 정보를 담고 있음.
 * @author pyohwan
 *
 */
public class BoardListInfo {

	private int page = 1;
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
