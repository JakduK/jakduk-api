package com.jakduk.api.model.simple;

import com.jakduk.api.model.embedded.CommonWriter;

/**
 * @author pyohwan
 * 16. 5. 10 오후 10:57
 */

public class JakduOnSchedule {

	private String id;
	private CommonWriter writer;
	private int homeScore;
	private int awayScore;

	public String getId() {
		return id;
	}

	public CommonWriter getWriter() {
		return writer;
	}

	public int getHomeScore() {
		return homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}
}
