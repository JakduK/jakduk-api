package com.jakduk.api.model.db;

import com.jakduk.api.common.Constants;

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

	@Id
	private String id;
	private String name;
	private Constants.CLUB_TYPE clubType;
	private Constants.CLUB_AGE age;
	private Constants.CLUB_SEX sex;

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

	public Constants.CLUB_TYPE getClubType() {
		return clubType;
	}

	public void setClubType(Constants.CLUB_TYPE clubType) {
		this.clubType = clubType;
	}

	public Constants.CLUB_AGE getAge() {
		return age;
	}

	public void setAge(Constants.CLUB_AGE age) {
		this.age = age;
	}

	public Constants.CLUB_SEX getSex() {
		return sex;
	}

	public void setSex(Constants.CLUB_SEX sex) {
		this.sex = sex;
	}
}
