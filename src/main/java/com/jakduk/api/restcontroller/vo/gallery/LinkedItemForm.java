package com.jakduk.api.restcontroller.vo.gallery;

import com.jakduk.api.common.Constants;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 4. 14..
 */

@Getter
public class LinkedItemForm {
    private String itemId; // 아이템 ID
    private Constants.GALLERY_FROM_TYPE from; // 출처
}
