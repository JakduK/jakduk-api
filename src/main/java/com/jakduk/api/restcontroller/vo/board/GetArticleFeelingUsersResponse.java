package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.embedded.CommonFeelingUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by pyohwanjang on 2017. 3. 11..
 */

@ApiModel(description = "자유게시판 글의 감정 표현 회원 목록")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticleFeelingUsersResponse {

    @ApiModelProperty(example = "2", value = "글번호")
    private Integer seq;

    @ApiModelProperty(value = "좋아요 회원 목록")
    private List<CommonFeelingUser> usersLiking;

    @ApiModelProperty(value = "싫어요 회원 목록")
    private List<CommonFeelingUser> usersDisliking;

}
