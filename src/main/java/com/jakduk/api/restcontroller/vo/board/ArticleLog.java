package com.jakduk.api.restcontroller.vo.board;

import com.jakduk.api.common.Constants;
import com.jakduk.api.model.embedded.CommonWriter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleLog {

    private String id;
    private Constants.ARTICLE_LOG_TYPE type;
    private CommonWriter writer; // 글쓴이
    private LocalDateTime timestamp; // 2017-07-18T00:25:45 Timestamp (ISO 8601)

}
