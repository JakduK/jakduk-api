package com.jakduk.api.model.web.board;

import com.jakduk.api.common.CoreConst;
import lombok.Data;

/**
 * 게시판의 현재 페이지와 분류에 대한 정보를 담고 있음.
 * @author pyohwan
 *
 */

@Data
public class BoardListInfo {

	private int page = 1;
	private int size = CoreConst.BOARD_MAX_LIMIT;
	private String category = CoreConst.BOARD_CATEGORY_NONE;
}
