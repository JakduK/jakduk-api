package com.jakduk.api.model.embedded;

import com.jakduk.api.common.Constants;
import lombok.*;

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
	private Constants.DEVICE_TYPE device;

}
