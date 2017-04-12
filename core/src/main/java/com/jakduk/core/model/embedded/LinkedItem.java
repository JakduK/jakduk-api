package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CoreConst;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 4. 10..
 */

@Builder
@Getter
public class LinkedItem {

    private String id;
    private CoreConst.GALLERY_FROM_TYPE from;

}
