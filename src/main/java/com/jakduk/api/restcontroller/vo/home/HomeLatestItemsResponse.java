package com.jakduk.api.restcontroller.vo.home;

import com.jakduk.api.model.db.HomeDescription;
import com.jakduk.api.model.simple.UserSimple;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 홈에서 보여질 데이터 들
 *
 * @author pyohwan
 * 16. 5. 7 오후 10:01
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class HomeLatestItemsResponse {
    private List<HomeArticle> articles; // 최근 글
    private List<UserSimple> users; // 최근 가입 회원
    private List<HomeGallery> galleries; // 최근 사진
    private List<HomeArticleComment> comments; // 최근 댓글
    private HomeDescription homeDescription; // 상단 글
}
