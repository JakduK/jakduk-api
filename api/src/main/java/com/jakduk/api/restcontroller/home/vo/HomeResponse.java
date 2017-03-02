package com.jakduk.api.restcontroller.home.vo;

import com.jakduk.core.model.simple.BoardFreeOnList;
import com.jakduk.core.model.simple.UserOnHome;
import com.jakduk.core.model.db.HomeDescription;
import com.jakduk.core.model.simple.BoardFreeCommentOnHome;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author pyohwan
 * 16. 5. 7 오후 10:01
 */

@ApiModel(description = "홈에서 보여질 데이터 들")
@Getter
@Setter
public class HomeResponse {

    @ApiModelProperty(value = "최근 글")
    private List<BoardFreeOnList> posts;

    @ApiModelProperty(value = "최근 가입 회원")
    private List<UserOnHome> users;

    @ApiModelProperty(value = "최근 사진")
    private List<GalleryOnHome> galleries;

    @ApiModelProperty(value = "최근 댓글")
    private List<BoardFreeCommentOnHome> comments;

    @ApiModelProperty(value = "상단 글")
    private HomeDescription homeDescription;

}
