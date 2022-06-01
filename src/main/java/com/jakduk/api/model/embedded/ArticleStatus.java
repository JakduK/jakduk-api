package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArticleStatus {
	private Boolean notice;
	private Boolean delete;
}
