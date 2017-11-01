package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;
import lombok.Builder;
import lombok.Getter;

/**
 * @author pyohwan
 *         16. 7. 21 오후 10:54
 */

@Builder
@Getter
public class DeleteArticleResponse {
    Constants.ARTICLE_DELETE_TYPE result;
}
