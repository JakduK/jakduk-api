package com.jakduk.api.model.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 11. 7.
 * @desc     :
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardFeelingCount {
	private String id;
	private Integer usersLikingCount;
	private Integer usersDislikingCount;

}
