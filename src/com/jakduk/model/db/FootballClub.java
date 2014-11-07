package com.jakduk.model.db;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.jakduk.model.embedded.FootballClubName;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 11.
 * @desc     :
 */

@Document
public class FootballClub {
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	@DBRef
	private FootballClubOrigin origin;

	private String active;
	
	private List<FootballClubName> names;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public FootballClubOrigin getOrigin() {
		return origin;
	}

	public void setOrigin(FootballClubOrigin origin) {
		this.origin = origin;
	}
	
	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public List<FootballClubName> getNames() {
		return names;
	}

	public void setNames(List<FootballClubName> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "FootballClub [id=" + id + ", origin=" + origin + ", active="
				+ active + ", names=" + names + "]";
	}

}
