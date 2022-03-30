package com.jakduk.api.restcontroller.vo.admin;

/**
 * Created by pyohwan on 16. 3. 5.
 */

public class MyJakduRequest {
	private int homeScore;
	private int awayScore;
	private String jakduScheduleId;

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public String getJakduScheduleId() {
		return jakduScheduleId;
	}
}
