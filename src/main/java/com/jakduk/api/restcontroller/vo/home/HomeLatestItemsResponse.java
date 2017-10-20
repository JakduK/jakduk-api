package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.model.simple.UserOnHome;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 * 16. 5. 7 오후 10:01
 */

@ApiModel(description = "홈에서 보여질 데이터 들")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class HomeLatestItemsResponse {

    @ApiModelProperty(value = "최근 글")
    private List<HomeArticle> articles;

    @ApiModelProperty(value = "최근 가입 회원")
    private List<UserOnHome> users;

    @ApiModelProperty(value = "최근 사진")
    private List<HomeGallery> galleries;

    @ApiModelProperty(value = "최근 댓글")
    private List<HomeArticleComment> comments;

    @ApiModelProperty(value = "상단 글")
    private HomeDescription homeDescription;

}
