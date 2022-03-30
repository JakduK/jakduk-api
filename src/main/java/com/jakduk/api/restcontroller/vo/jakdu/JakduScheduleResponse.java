package com.jakduk.api.restcontroller.vo.jakdu;

import com.jakduk.api.model.db.JakduSchedule;
import com.jakduk.api.model.embedded.LocalName;

import java.util.List;
import java.util.Map;

/**
 * @author pyohwan
 * 16. 3. 26 오후 11:28
 */

public class JakduScheduleResponse {
	private Map<String, LocalName> fcNames;
	private Map<String, LocalName> competitionNames;
	private List<JakduSchedule> schedules;

	public Map<String, LocalName> getFcNames() {
		return fcNames;
	}

	public void setFcNames(Map<String, LocalName> fcNames) {
		this.fcNames = fcNames;
	}

	public Map<String, LocalName> getCompetitionNames() {
		return competitionNames;
	}

	public void setCompetitionNames(Map<String, LocalName> competitionNames) {
		this.competitionNames = competitionNames;
	}

	public List<JakduSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<JakduSchedule> schedules) {
		this.schedules = schedules;
	}
}
