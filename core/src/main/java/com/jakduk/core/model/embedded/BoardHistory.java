package com.jakduk.core.model.embedded;

import com.jakduk.core.common.CoreConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

@AllArgsConstructor
@Setter
@Getter
public class BoardHistory {
	
	@Id
	private String id;
	
	private CoreConst.BOARD_HISTORY_TYPE type;

	// TODO : @DBRef User 객체로 변환하자
	private CommonWriter writer;
	
}
