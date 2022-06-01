package com.jakduk.api.model.simple;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.api.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by pyohwanjang on 2017. 3. 14..
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = Constants.COLLECTION_ARTICLE)
public class ArticleOnSitemap {

	private String id;
	private String board;
	private Integer seq;
	private LocalDateTime lastUpdated;

}
