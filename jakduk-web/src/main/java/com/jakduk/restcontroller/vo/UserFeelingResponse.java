package com.jakduk.restcontroller.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.common.CommonConst;
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
    private CommonConst.FEELING_TYPE feeling;

    @ApiModelProperty(value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(value = "싫어요 수")
    private Integer numberOfDislike;
}
