package com.jakduk.api.model.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 10.
 * @desc     :
 */

@Document
public class AttendanceLeague {

	@Id
	private String id;
	@DBRef
	private Competition competition;
	private Integer season;
	private Integer games;
	private Integer total;
	private Integer average;
	private Integer numberOfClubs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Competition getCompetition() {
		return competition;
	}

	public void setCompetition(Competition competition) {
		this.competition = competition;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getGames() {
		return games;
	}

	public void setGames(Integer games) {
		this.games = games;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getAverage() {
		return average;
	}

	public void setAverage(Integer average) {
		this.average = average;
	}

	public Integer getNumberOfClubs() {
		return numberOfClubs;
	}

	public void setNumberOfClubs(Integer numberOfClubs) {
		this.numberOfClubs = numberOfClubs;
	}
}
