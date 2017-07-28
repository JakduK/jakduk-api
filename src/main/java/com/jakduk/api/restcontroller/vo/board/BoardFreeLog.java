package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.JakdukConst;
import com.jakduk.api.model.embedded.CommonWriter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardFreeLog {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "사진 ID")
    private String id;

    private JakdukConst.BOARD_FREE_HISTORY_TYPE type;

    @ApiModelProperty(value = "글쓴이")
    private CommonWriter writer;

    @ApiModelProperty(example = "2017-07-18T00:25:45", value = "Timestamp (ISO 8601)")
    private LocalDateTime timestamp;

}
