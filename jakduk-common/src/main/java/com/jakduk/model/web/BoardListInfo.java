package com.jakduk.model.web;

import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * 게시판의 현재 페이지와 분류에 대한 정보를 담고 있음.
 * @author pyohwan
 *
 */

@Data
public class BoardListInfo {

	private int page = 1;
	private int size = CommonConst.BOARD_MAX_LIMIT;
	private String category = CommonConst.BOARD_CATEGORY_NONE;
}
