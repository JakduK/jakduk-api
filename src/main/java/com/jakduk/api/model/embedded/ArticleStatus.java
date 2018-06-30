package com.jakduk.api.model.embedded;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArticleStatus {
	private Boolean notice;
	private Boolean delete;
}
