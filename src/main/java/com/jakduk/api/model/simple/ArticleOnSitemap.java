package com.jakduk.api.model.simple;

import com.jakduk.api.common.Constants;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by pyohwanjang on 2017. 3. 14..
 */

@Getter
@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleOnSitemap {

    private String id;
    private String board;
    private Integer seq;
    private LocalDateTime lastUpdated;

}
