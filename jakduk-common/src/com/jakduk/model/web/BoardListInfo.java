package com.jakduk.model.web;

import com.jakduk.common.CommonConst;

/**
 * 게시판의 현재 페이지와 분류에 대한 정보를 담고 있음.
 * @author pyohwan
 *
 */
public class BoardListInfo {

	private int page = 1;
	private int size = CommonConst.BOARD_MAX_LIMIT;
	private String category = CommonConst.BOARD_CATEGORY_NONE;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "BoardListInfo [page=" + page + ", size=" + size + ", category=" + category + "]";
	}
}
