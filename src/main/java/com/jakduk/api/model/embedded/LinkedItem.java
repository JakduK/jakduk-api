package com.jakduk.api.model.embedded;

import com.jakduk.api.common.JakdukConst;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 4. 10..
 */

@Builder
@Getter
public class LinkedItem {

    private String id;
    private JakdukConst.GALLERY_FROM_TYPE from;

}
