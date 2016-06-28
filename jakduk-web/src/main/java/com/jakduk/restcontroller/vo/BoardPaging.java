package com.jakduk.restcontroller.vo;

import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * Created by pyohwan on 16. 6. 26.
 */

@Data
public class BoardPaging {
    private int page = 1;
    private int size = CommonConst.BOARD_MAX_LIMIT;
    private CommonConst.BOARD_CATEGORY_TYPE category = CommonConst.BOARD_CATEGORY_TYPE.ALL;
}
