package com.jakduk.api.vo.board;

import com.jakduk.core.model.simple.BoardFreeSimple;
import io.swagger.annotations.ApiModel;
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

@ApiModel(description = "자유게시판 글 상세")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FreePostDetailResponse {

    @ApiModelProperty(value = "글 상세")
    private FreePostDetail post;

    @ApiModelProperty(value = "앞 글")
    private BoardFreeSimple prevPost;

    @ApiModelProperty(value = "뒷 글")
    private BoardFreeSimple nextPost;

    @ApiModelProperty(value = "작성자의 최근 글")
    private List<LatestFreePost> latestPostsByWriter;

}
