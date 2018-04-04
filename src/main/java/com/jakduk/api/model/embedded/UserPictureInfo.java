package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 2. 27..
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPictureInfo {

    private String id;
    private String smallPictureUrl;
    private String largePictureUrl;

}
