package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CoreConst;
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
@Setter
@Getter
public class BoardStatus {
	
	private Boolean notice;
	
	private Boolean delete;
	
	private CoreConst.DEVICE_TYPE device;
}
