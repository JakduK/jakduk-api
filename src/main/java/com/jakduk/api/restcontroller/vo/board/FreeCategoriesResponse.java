package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.model.db.BoardCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pyohwan
 *         16. 7. 18 오후 9:55
 */

@ApiModel(value = "자유게시판 말머리 목록")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FreeCategoriesResponse {

    @ApiModelProperty(value = "말머리 목록")
    private List<BoardCategory> categories;
}
