package com.jakduk.api.model.aggregate;

import com.jakduk.api.model.db.FootballClub;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 2. 16.
 * @desc     :
 */

public class SupporterCount {
	private FootballClub supportFC;
	private Integer count;

	public FootballClub getSupportFC() {
		return supportFC;
	}

	public Integer getCount() {
		return count;
	}
}
