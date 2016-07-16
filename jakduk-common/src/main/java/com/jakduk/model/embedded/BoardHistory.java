package com.jakduk.model.embedded;

import com.jakduk.common.CommonConst;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 1. 11.
 * @desc     :
 */

@AllArgsConstructor
@Setter
public class BoardHistory {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private CommonConst.BOARD_HISTORY_TYPE type;
	
	private CommonWriter writer;
	
}
