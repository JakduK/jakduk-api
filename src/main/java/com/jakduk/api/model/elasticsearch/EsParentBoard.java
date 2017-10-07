package com.jakduk.api.model.elasticsearch;

import lombok.Getter;

/**
 * Created by pyohwan on 16. 12. 4.
 */

@Getter
public class EsParentBoard {
    private String id;
    private String board;
    private String category;
    private Integer seq;
    private String subject;
}
