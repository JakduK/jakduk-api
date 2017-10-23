package com.jakduk.api.model.embedded;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 2. 27..
 */

@NoArgsConstructor
@Getter
public class UserPictureInfo {

    private String id;
    private String smallPictureUrl;
    private String largePictureUrl;

    public UserPictureInfo(String id, String smallPictureUrl, String largePictureUrl) {
        this.id = id;
        this.smallPictureUrl = smallPictureUrl;
        this.largePictureUrl = largePictureUrl;
    }

}
