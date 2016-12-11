package com.jakduk.api.restcontroller.vo;

import com.jakduk.core.common.CoreConst;
import lombok.Data;

/**
 * Created by pyohwan on 16. 6. 26.
 */

@Data
public class BoardPaging {
    private int page = 1;
    private int size = CoreConst.BOARD_MAX_LIMIT;
    private CoreConst.BOARD_CATEGORY_TYPE category = CoreConst.BOARD_CATEGORY_TYPE.ALL;
}
