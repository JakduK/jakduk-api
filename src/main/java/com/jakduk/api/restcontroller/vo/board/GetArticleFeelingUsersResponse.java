package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.embedded.CommonFeelingUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 자유게시판 글의 감정 표현 회원 목록
 *
 * Created by pyohwanjang on 2017. 3. 11..
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticleFeelingUsersResponse {

    private Integer seq; // 글번호
    private List<CommonFeelingUser> usersLiking; // 좋아요 회원 목록
    private List<CommonFeelingUser> usersDisliking; // 싫어요 회원 목록

}
