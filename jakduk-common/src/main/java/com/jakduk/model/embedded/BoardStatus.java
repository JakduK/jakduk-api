package com.jakduk.model.embedded;

import com.jakduk.common.CommonConst;
import lombok.Data;
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
	
	private CommonConst.DEVICE_TYPE device;

	public BoardStatus(CommonConst.DEVICE_TYPE device) {
		this.device = device;
	}

}
