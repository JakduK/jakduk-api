package com.jakduk.api.model.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwan on 16. 12. 4.
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EsParentArticle {
    private String id;
    private Integer seq;
    private String board;
    private String category;
    private String subject;
}
