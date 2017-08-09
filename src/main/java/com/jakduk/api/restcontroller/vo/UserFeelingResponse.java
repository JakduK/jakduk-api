package com.jakduk.api.restcontroller.vo;

import com.jakduk.api.common.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:57
 */

@ApiModel(description = "감정표현")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserFeelingResponse {

    @ApiModelProperty(example = "LIKE", value = "나의 감정 표현 종류")
    @Setter
    private Constants.FEELING_TYPE myFeeling;

    @ApiModelProperty(example = "5", value = "좋아요 수")
    private Integer numberOfLike;

    @ApiModelProperty(example = "5", value = "싫어요 수")
    private Integer numberOfDislike;
}
