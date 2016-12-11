package com.jakduk.api.restcontroller.vo;

import com.jakduk.core.common.CoreConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:57
 */

@ApiModel(value = "감정표현")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserFeelingResponse {

    @ApiModelProperty(value = "감정 표현 종류")
    private CoreConst.FEELING_TYPE feeling;

    @ApiModelProperty(value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(value = "싫어요 수")
    private Integer numberOfDislike;
}
