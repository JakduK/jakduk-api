package com.jakduk.api.vo.gallery;

import com.jakduk.api.common.CoreConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 4. 14..
 */

@Getter
@ApiModel(value = "연관된 아이템 폼")
public class LinkedItemForm {

    @ApiModelProperty(example = "58b9050b807d714eaf50a111", value = "아이템 ID")
    private String itemId;

    @ApiModelProperty(value = "출처")
    private CoreConst.GALLERY_FROM_TYPE from;

}
