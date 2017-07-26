package com.jakduk.api.restcontroller.admin.vo.board;

import com.jakduk.api.common.JakdukConst;
import lombok.Data;

/**
 * 게시판의 현재 페이지와 분류에 대한 정보를 담고 있음.
 * @author pyohwan
 *
 */

@Data
public class BoardListInfo {

	private int page = 1;
	private int size = JakdukConst.BOARD_MAX_LIMIT;
	private String category = JakdukConst.BOARD_CATEGORY_NONE;
}
