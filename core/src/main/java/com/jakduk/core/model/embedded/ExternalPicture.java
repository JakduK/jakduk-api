package com.jakduk.core.model.embedded;

import lombok.Builder;
import lombok.Getter;

/**
 * SNS계정의 사진URL을 담는다.
 *
 * Created by pyohwanjang on 2017. 2. 25..
 */

@Builder
@Getter
public class ExternalPicture {

    private String smallPictureUrl;

    private String largePictureUrl;
}
