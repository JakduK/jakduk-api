package com.jakduk.api.model.aggregate;

/**
* @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
* @company  : http://jakduk.com
* @date     : 2015. 11. 7.
* @desc     :
*/

public class BoardFeelingCount {
    private String id;
	private Integer usersLikingCount;
	private Integer usersDislikingCount;

	public String getId() {
		return id;
	}

	public Integer getUsersLikingCount() {
		return usersLikingCount;
	}

	public Integer getUsersDislikingCount() {
		return usersDislikingCount;
	}
}
