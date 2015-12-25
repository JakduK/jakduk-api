package com.jakduk.model.db;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author <a href="mailto:phjang1983@daum.net">Jang,Pyohwan</a>
 * @company  : http://jakduk.com
 * @date     : 2014. 9. 28.
 * @desc     :
 */

@Document
public class FootballClubOrigin {

	enum CLUB_TYPE {
		FOOTBALL_CLUB,
		NATIONAL_TEAM
	}

	enum AGE_TYPE {
		UNDER_15,
		UNDER_17,
		UNDER_20,
		UNDER_23,
		SENIOR
	}

	enum SEX_TYPE {
		MEN,
		WOMEN
	}
	
	@Id  @GeneratedValue(strategy=GenerationType.AUTO)
	private String id;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "FootballClubOrigin [id=" + id + ", name=" + name + "]";
	}

}
