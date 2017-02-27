package com.jakduk.api.configuration.authentication.user;

import com.jakduk.core.common.CoreConst;
import com.jakduk.core.model.db.UserPicture;
import lombok.Getter;

/**
 * Created by pyohwanjang on 2017. 2. 27..
 */

@Getter
public class UserDetailsPicture {

    private String id;
    private CoreConst.ACCOUNT_TYPE sourceType;
    private String smallPictureUrl;
    private String largePictureUrl;

    public UserDetailsPicture(UserPicture userPicture, String smallPictureUrl, String largePictureUrl) {

        this.id = userPicture.getId();
        this.sourceType = userPicture.getSourceType();
        this.smallPictureUrl = smallPictureUrl;
        this.largePictureUrl = largePictureUrl;
    }

}
