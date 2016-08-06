package com.jakduk.core.model.web;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */
public class AttendanceClubWrite {

	@Id
	private String id;
	
	@NotEmpty
	private String origin;
	
	@NotEmpty
	private String league;
	
	@NotNull
	private Integer season;
	
	@NotNull
	private Integer games;
	
	@NotNull
	private Integer total;
	
	@NotNull
	private Integer average;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
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

	@Override
	public String toString() {
		return "AttendanceClubWrite [id=" + id + ", origin=" + origin
				+ ", league=" + league + ", season=" + season + ", games="
				+ games + ", total=" + total + ", average=" + average + "]";
	}
	
}
