package com.jakduk.api.restcontroller.vo.board;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author pyohwan
 *         16. 7. 10 오후 11:52
 */

@ApiModel(description = "자유게시판 글 목록")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetArticlesResponse {

    @ApiModelProperty(value = "말머리 맵")
    private Map<String, String> categories;

    @ApiModelProperty(value = "글 목록")
    private List<GetArticle> articles;

    @ApiModelProperty(value = "공지글 목록")
    private List<GetArticle> notices;

    @ApiModelProperty(value = "마지막 페이지 여부")
    private Boolean last;

    @ApiModelProperty(value = "첫 페이지 여부")
    private Boolean first;

    @ApiModelProperty(value = "전체 페이지 수")
    private Integer totalPages;

    @ApiModelProperty(value = "페이지당 글 수")
    private Integer size;

    @ApiModelProperty(value = "현재 페이지(0부터 시작)")
    private Integer number;

    @ApiModelProperty(value = "현제 페이지에서 글 수")
    private Integer numberOfElements;

    @ApiModelProperty(value = "전체 글 수")
    private Long totalElements;

}
