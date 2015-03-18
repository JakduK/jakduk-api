package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2015. 3. 18.
 * @desc     :
 */

@Document
public class AttendanceClub {
	
	/**
	 * ID
	 */
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@DBRef
	private FootballClubOrigin club;
	
	private String league;
	
	private Integer season;
	
	private Integer games;
	
	private Integer total;
	
	private Integer average;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FootballClubOrigin getClub() {
		return club;
	}

	public void setClub(FootballClubOrigin club) {
		this.club = club;
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
		return "AttendanceClub [id=" + id + ", club=" + club + ", league="
				+ league + ", season=" + season + ", games=" + games
				+ ", total=" + total + ", average=" + average + "]";
	}

}
