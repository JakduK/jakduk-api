package com.jakduk.restcontroller.vo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 26.
 */

@Data
@JsonTypeName(value = "response")
@JsonTypeInfo(use=JsonTypeInfo.Id.NONE, include= JsonTypeInfo.As.WRAPPER_OBJECT)
public class UserFeelingResponse {
    private CommonConst.FEELING_TYPE feeling;
    private Integer numberOfLike;
    private Integer numberOfDislike;
}
