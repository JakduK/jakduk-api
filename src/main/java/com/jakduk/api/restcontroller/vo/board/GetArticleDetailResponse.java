package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.simple.ArticleSimple;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 16 오후 7:28
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticleDetailResponse {

    @ApiModelProperty(value = "글 상세")
    private ArticleDetail article;

    @ApiModelProperty(value = "앞 글")
    private ArticleSimple prevArticle;

    @ApiModelProperty(value = "뒷 글")
    private ArticleSimple nextArticle;

    @ApiModelProperty(value = "작성자의 최근 글")
    private List<LatestArticle> latestArticlesByWriter;

}
