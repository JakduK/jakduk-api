package com.jakduk.vo;

import com.jakduk.common.CommonConst;
import lombok.Data;

/**
 * Created by pyohwan on 16. 3. 26.
 */

@Data
public class UserFeelingResponse {
    private CommonConst.FEELING_TYPE feeling;
    private Integer numberOfLike;
    private Integer numberOfDislike;
}
