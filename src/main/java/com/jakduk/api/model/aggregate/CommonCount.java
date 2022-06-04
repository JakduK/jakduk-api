package com.jakduk.api.model.aggregate;

import com.jakduk.api.model.embedded.ArticleItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 12. 30.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommonCount {
	private ArticleItem id;
	private Integer count;
}