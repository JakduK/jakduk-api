package com.jakduk.api.vo.board;

import com.jakduk.api.common.ApiConst;
import com.jakduk.core.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardFreeHistory {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    private ApiConst.BOARD_FREE_HISTORY_TYPE type;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "2017-07-18T00:25:45", value = "Timestamp (ISO 8601)")
    private LocalDateTime timestamp;

}
