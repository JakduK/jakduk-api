package com.jakduk.api.vo.board;

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
public class FreePostsResponse {

    @ApiModelProperty(value = "말머리 맵")
    Map<String, String> categories;

    @ApiModelProperty(value = "글 목록")
    List<FreePost> posts;

    @ApiModelProperty(value = "공지글 목록")
    List<FreePost> notices;

    @ApiModelProperty(value = "마지막 페이지 여부")
    boolean last;

    @ApiModelProperty(value = "첫 페이지 여부")
    boolean first;

    @ApiModelProperty(value = "전체 페이지 수")
    int totalPages;

    @ApiModelProperty(value = "페이지당 글 수")
    int size;

    @ApiModelProperty(value = "현재 페이지(0부터 시작)")
    int number;

    @ApiModelProperty(value = "현제 페이지에서 글 수")
    int numberOfElements;

    @ApiModelProperty(value = "전체 글 수")
    long totalElements;
}
