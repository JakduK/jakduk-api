package com.jakduk.api.restcontroller.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by pyohwanjang on 2017. 2. 25..
 */

@Getter
public class UserPicture {

    @ApiModelProperty(example = "https://img1.daumcdn.net/thumb/R55x55/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876", value = "회원 작은 사진")
    private String smallPictureUrl;

    @ApiModelProperty(example = "https://img1.daumcdn.net/thumb/R158x158/?fname=http%3A%2F%2Ftwg.tset.daumcdn.net%2Fprofile%2FSjuNejHmr8o0&t=1488000722876", value = "회원 큰 사진")
    private String largePictureUrl;
}
